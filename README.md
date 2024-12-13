# Qr1Gen

![screenshot](screenshot.png)

**A QR Code generator based on time.**

The QR code data also relies on two parameters: a public `id` and a private `number`.

You can import and export the app configuration in the settings activity.

This is a proof of concept of an Android app developped and built by a Makefile:
- It is fully written with the Java programming language (version 8)
- It uses only android native library
- Layout is defined in a programming way without XML

# Build

You need to install several components of the android SDK:
- build tools
- platform tools
- platform (set by default to level 26 in the project)

The recommanded way is to use `sdkmanager` cli provided by the [command tools](https://developer.android.com/studio?hl=fr#command-tools)

You have to create a `.env` file and define the following keys:
- `KEYSTORE`: the path of the keystore file (usually with jks extension)
- `KEYSTORE_PASSWORD`: the password associated to your keystore

The Makefile can generate automatically a basic keystore using these informations.

You can compile the app with the command: `make`

Then, install the app on an emulator or directly on your phone: `make install`

# Credits

iconixar on Flaticon for the app icon