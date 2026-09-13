@file:Suppress("SpellCheckingInspection", "GrazieInspection")

package com.leohearts.alternativeunlockhook

import android.annotation.SuppressLint
import android.app.AndroidAppHelper
import android.text.format.DateFormat
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.FileNotFoundException
import java.io.FileReader
import java.security.MessageDigest
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Properties

class HookClass : IXposedHookLoadPackage {
    // NOTE: When modifying this, make sure credential sufficiency validation logic is intact.
    companion object {
        const val TAG: String = "alternativeUnlockHook"

        // wait if you stored inside `com.android.systemui`, why move to here? for dual-user support? `/data/user/0/` mightve worked
        const val CONFIG_PATH: String = "/data/local/tmp/alternativePass.properties"
    }

    private var fakePassword: String = "114514"
    private var realPassword: String = "1919810"
    private var actionType: String = "sh"
    private var actionCommand: String = "whoami"
    private var dynamicLoad: String = "false"
    private var timeIsPIN: String = "false"

    @SuppressLint("SdCardPath")
    fun initConfig() {
        try {
            val properties = Properties()
            val f: FileReader = try {
                FileReader(CONFIG_PATH)
            } catch (_: FileNotFoundException) {
                FileReader("/data/data/com.android.systemui/alternativePass.properties") // make sure module can work if migration process hasn't been started
            }
            properties.load(f)
            fakePassword = properties.getProperty("fakePassword", "114514")
            realPassword = properties.getProperty(
                "realPassword", "1919810"
            ) // nobody sets 1919810 as real password, right ???
            actionType = properties.getProperty("actionType", "sh")
            actionCommand =
                properties.getProperty("actionCommand", "whoami") // dont do anything if unset
            dynamicLoad = properties.getProperty("dynamicLoad", "false")
            timeIsPIN = properties.getProperty("timeIsPIN", "false")
        } catch (e: Exception) {
            if (e.javaClass != FileNotFoundException::class.java) {
                e.printStackTrace()
            }
        }
    }

    private fun unlock(param: MethodHookParam, credType: Int) {
        try {
            when (actionType.trim().lowercase()) {
                "sh", "shell" -> RootShell.system(actionCommand)
                "su", "sudo" -> RootShell.sudo(actionCommand)
                else -> Log.w(TAG, "unknown actionType '$actionType'")
            }
        } catch (e: Exception) {
            Log.e(TAG, "action failed: $e")
        }
        try {
            val clazz = param.args[0].javaClass // read before overwriting
            param.args[0] = try {
                XposedHelpers.newInstance(clazz, credType, realPassword.toByteArray())
            } catch (e: NoSuchMethodError) {
                Log.e(TAG, "$e")
                XposedHelpers.newInstance(clazz, credType, realPassword)
            }
            Log.i(TAG, "unlock: credential replaced")
        } catch (t: Throwable) {
            Log.e(TAG, "unlock: replacement failed: $t")
        }
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        Log.i(TAG, "handleLoadPackage: Loaded app")
        initConfig()
        val lockPatternUtils = XposedHelpers.findClass(
            "com.android.internal.widget.LockPatternUtils", lpparam.classLoader
        )
        XposedBridge.hookAllMethods(lockPatternUtils, "checkCredential", object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (dynamicLoad == "true") initConfig()
                val mCredential = param.args[0]
                val credType = XposedHelpers.callMethod(mCredential, "getType") as Int
                val cred = XposedHelpers.callMethod(mCredential, "getCredential") as ByteArray
                val attemptedStr = String(cred, Charsets.UTF_8)
                if (MessageDigest.isEqual(cred, realPassword.toByteArray())) {
                    Log.i(TAG, "device password detected, suppressing logs")
                } else {
                    Log.d(
                        TAG,
                        "credType: $credType attemptedStr: $attemptedStr " + "credBytes: ${cred.size} ${cred.contentToString()}"
                    )
                }
                fun sha256(input: String): ByteArray =
                        MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
                if (timeIsPIN == "false") {
                    if (MessageDigest.isEqual(sha256(attemptedStr), sha256(fakePassword))) {
                        Log.i(TAG, "fakePassword matched")
                        unlock(param, credType)
                    } else {
                        Log.i(TAG, "fakePassword did not match")
                    }
                } else if (timeIsPIN == "true") {
                    val context = AndroidAppHelper.currentApplication()
                    val is24hour = context?.let { DateFormat.is24HourFormat(it) } ?: false
                    val hourPattern = if (is24hour) "HH" else "hh"
                    val now = LocalTime.now()
                    var time = now.format(DateTimeFormatter.ofPattern(hourPattern)) + now.format(
                        DateTimeFormatter.ofPattern("mm")
                    )
                    val zeros = "0".repeat(maxOf(realPassword.length - time.length, 0))
                    time += zeros
                    Log.i(TAG, time)
                    if (MessageDigest.isEqual(sha256(attemptedStr), sha256(time))) {
                        Log.i(TAG, "Time matched")
                        unlock(param, credType)
                    } else {
                        Log.i(TAG, "Time did not match")
                    }
                }
            }
        })
    }
}
