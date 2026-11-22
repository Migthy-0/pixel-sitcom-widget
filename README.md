# Pixel 9 Sitcom Laugh Widget 😂📱

Minimalist native Android (Kotlin + Material You Dynamic Color) Home Screen Widget for Google Pixel 9 (Android 15, `targetSdk = 35`) that plays classic sitcom canned laughter (`R.raw.sitcom_laugh`) immediately on tap without opening an activity.

## Features
- **Instant Home Screen Playback:** Uses `AppWidgetProvider` + `PendingIntent.getBroadcast` + `MediaPlayer` to play `res/raw/sitcom_laugh.mp3` directly from the home screen.
- **Material You Dynamic Color:** Adapts automatically to the Pixel 9 wallpaper via `@android:color/system_accent1_*` and `@android:color/system_neutral1_*`.
- **1-Tap Widget Pinning:** Companion `MainActivity` includes a direct button (`AppWidgetManager.requestPinAppWidget`) to pin the widget to the Pixel Launcher.
- **Cloud APK Builder:** GitHub Actions automatically compiles `app-debug.apk` on every push.
