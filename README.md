<p align="center">
<image style="height:200px;display:inline" src="fastlane/metadata/android/en-US/images/icon.png" height="200px" />
<h1 align="center">AlternativeUnlockXposed</h1>
<small><p align="center">
<a href='https://github.com/leohearts/AlternativeUnlockXposed/releases'><img src='https://img.shields.io/github/v/release/leohearts/AlternativeUnlockXposed?logo=github' /></a>
<a href='https://apt.izzysoft.de/fdroid/index/apk/com.leohearts.alternativeUnlockHook'><img src='https://img.shields.io/endpoint?url=https://apt.izzysoft.de/fdroid/api/v1/shield/com.leohearts.alternativeUnlockHook&logo=data:image/webp;base64,UklGRuIDAABXRUJQVlA4WAoAAAAQAAAALwAALwAAQUxQSPsBAAABkARJtmlb/Wx+27aNkW3bxsi2bRsj27Zt+9m4cfrjnrX25zgiJgD/tbnbzDj6iU6T3p31ATqfVHZWNLpB+UAgSyzl7/xFec9SecEF2EtlA0irRlGZUgDoQuV6SAtFUzsByBKmeJdG4v+K2sfuwC4qm0C6hepKQBMqd0Fan+olQJp3irAsEpcHqrDhy1bdpLILpDWotqg/DfF8nX3UuZO2+WRXzVmrYTbMHFMym0k0ZpHdzUTKHBKSI828kJ1T/BybFRg6011yTPR4voEhQI4EnvUVzJdYHyJ0Z9yAIyTPeNo1kBgdAnTkzyPsvKPtYoz0QppwJwkZbbDEbp2J1MbYTOfz7XI7nG3oaCKqQi3axgfZYLmTryEVTHwo/NaO7ewC3vzUFgGWgadLKdxkhxKR5H4AyQbOW5IXAhR7EZcNaEPtoaIF71HsJ4BXVSBNmGonRlFeUPLzeqrn5U9SlFLUoL7PWf4K/8cKi+QmanPJ5lP5kWSixuEtKu1QXL9qWVQfhdTtOuWO0ntosIVoOJXT4VMrXPXYVZI/SfHYC1hJdT1IT1FZA6hK9SFIu1C5HvB6rnqXVpImTBGWBphM7fMckG6nsg1QzKG5kAbSJlTuAlyuUh421AVS/7eK2KzAUEo/7RnsC3n6msrCAKp0HDZ+/PjhPZpUzhuA/1wAVlA4IMABAABQCgCdASowADAAPwlwr1IrpiQirjQMyXAhCWwAnTMqtzeucUlw48VcxjFbZPzAfrv+wHvAfiB7gPQA/Z3rCvQA8tL2Mf7l/z/SeCQhmQb/EIQWZ0+07TkHF8sAAP7bLoomj0upt+t0mhnKfsBJdQWfkc5HNWKaPkNKhp088Cr2NAhIfPmDnBDZOUt7By7cwzxZN//5FdkaQs8crvXkKa0frHu9cXoG0F3yaK1nFCtTgoVareERAdcC1DjxQLsbCQ4aNTqYK9UxZ34k/dpbiOQF09m9Hf2TgYIDB/Sxvb42ETZqud4mU3LePoUv8IOrm+XD8IvE/KFI1glpJeMpU0/xDMivgMfPgZ5cQpnDmu7uBectIDGhRgm3oe+ATIKT4E5P91uz9iW/6WNgjfmJdDrQ3oy55RYAuJWoAC1lqSeB67Nj0r5s3W6Mjl3B1kErxQf76ymYH+lmAmKwWyjSFfNX6a2DM70BLyfEd4QR4Dtv8Ybr61nzkZ5BvD1f2VMRWN0WzLvT+Fg0201HQA+NFY1vsQYX8ciD/wxpUC9+OZk7CU8xh0uMZaa0Af+sNbz0ZIW6H1JLDQL/yOGWhLrDTQAA' /></a>
<a href='https://shields.rbtlog.dev/com.leohearts.alternativeUnlockHook'><img src='https://shields.rbtlog.dev/simple/com.leohearts.alternativeUnlockHook' alt='RB Shield'></a>
</p></small>
<b><i><p align="center">Unlock your Android phone with an alternative PIN (Xposed, Root)</p></i></b>
</p>

This app provides a way to run something when providing a specific, secondary PIN on your lock screen.

Unlike [Duress](https://play.google.com/store/apps/details?id=me.lucky.duress&hl=en&gl=US), this app uses Xposed Framework so you can also unlock your phone with a *wrong* PIN, preventing some *social engineering vulnerabilities*😇 And it also works in BFU state.

> [!WARNING]
> This module *cannot* protect you from targeted attacks and/or forensics. You may need more security methods to deal with a complicated threat model.

## Features

- Alternative PIN to unlock phone
- Run command on alternative PIN, with root
- Easy to use user interface
- Material 3 Expressive design

Currently tested on:
- Android 15 (arm64, LOS 22.1)
- Android 14 (arm64)
- Android 13 (x86_64)
- Android 12 (x64_64)
- Android 11 (arm64, LOS 18.1)

Currently NOT working on:
- Android <= 10
- Samsung One UI
- OnePlus OxygenOS/ColorOS
- Tecno HiOS
- Most other non-AOSP

It *should* also work on other architectures

## How to use

- Install a root manager (Magisk, KernelSU, etc)
- Install an Xposed Manager (LSPosed, [Vector](https://github.com/JingMatrix/Vector))
- Install this module
- Activate this module in the Xposed manager and add the requested apps to the scope
- Launch AlternativeUnlockXposed, allow root access, set your primary (real) password and alternative password
- (optional) Setup what to do when entered the alternative PIN: change action to sudo, and set your command.
e.g. : ``for i in `pm list packages | grep -i -E 'telegram|sagernet|twitter|discord|tinder' | cut -d : -f 2` ; do pm disable $i; done``
- Test your unlock after click "Restart SystemUI"

> [!TIP]
> You can use automation software to make it easier to customize commands! just use a command like `am broadcast -a safety.intent.test` and catch the intent in your favorite app, it could be extended to do some stuff like take a picture, record audio, send a email, etc. like [Automate](https://llamalab.com/automate/). Note this kind of software can't work before unlock, so just make it an addition to your commands separated with a semicolon (`;`). 

If you are using this software, please consider to give it a star ⭐ on [Github](https://github.com/leohearts/AlternativeUnlockXposed) so we can know how many people are using it, since it doesn't contain any kind of tracking code.

## Download 

<a href='https://github.com/leohearts/AlternativeUnlockXposed/releases'><img width=200px src='https://img.shields.io/github/v/release/leohearts/AlternativeUnlockXposed?logo=github' /></a>

<a href='https://apt.izzysoft.de/fdroid/index/apk/com.leohearts.alternativeUnlockHook'><img width=200px src='https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroidButton.svg' /></a>

## Roadmap
- [x] Support PIN unlock
- [x] Run custom command on alternative PIN
- [x] User interface
- [ ] Run different commands on multiple fake password
- [x] Support more lockscreen modes
- [ ] Zygisk version (?)
- [ ] Require authentication for settings activity
- [ ] Option to hide the app from launcher ~~(I don't need it because I use SmartLauncher)~~

## Screenshots

<img width=30% src="fastlane/metadata/android/en-US/images/phoneScreenshots/01.png"> <img width=30% src="fastlane/metadata/android/en-US/images/phoneScreenshots/02.png">

## How does it work ?

When the fake password is provided, this module detects the input and replaces it with the real password. As a result, both the fake and real passwords can unlock the device, regardless of whether it is the first unlock or not.

Furthermore, since the fake password will be replaced with the real one, the fake password can also successfully decrypt the phone after a reboot.

## Credit

- [Duress](https://play.google.com/store/apps/details?id=me.lucky.duress&hl=en&gl=US) (for this idea)
- Google Bard (for app icon)
