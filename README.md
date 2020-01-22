# Sprouts

![logo](android/res/drawable-xxhdpi/ic_launcher.png)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/239591577a9a4a83ab80bbd2f6fcfdea)](https://app.codacy.com/app/Glusk2/sprouts?utm_source=github.com&utm_medium=referral&utm_content=Glusk2/sprouts&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.com/Glusk/sprouts.svg?branch=master)](https://travis-ci.com/Glusk/sprouts)
[![Build status](https://ci.appveyor.com/api/projects/status/8p0xficdfjn90odb/branch/master?svg=true)](https://ci.appveyor.com/project/Glusk2/sprouts/branch/master)
[![Coverage Status](https://coveralls.io/repos/github/Glusk2/sprouts/badge.svg?branch=master)](https://coveralls.io/github/Glusk2/sprouts?branch=master)
[![LoC](https://tokei.rs/b1/github/Glusk2/sprouts)](https://github.com/Glusk2/sprouts)

A multi-platform implementation of
[Sprouts](https://en.wikipedia.org/wiki/Sprouts_(game)),
built with [libGDX](https://github.com/libgdx/libgdx).

## Getting started

These instructions will get you a copy of the project up and running on your
local machine for development and testing purposes.

### Prerequisites

You need the following before installing Sprouts:

-   Java 8 (or later)

-   [Android SDK](https://developer.android.com/studio/#command-tools)
    -   required packages:
        -   `platforms;android-28`
        -   `platform-tools`
        -   `build-tools;28.0.3`
        -   `docs` (optional, required to generate Javadocs)

Here is how you can install all of the required Android SDK packages in one
line

```bash
$: ./ANDROID_HOME/tools/bin/sdkmanager "platforms;android-28" "platform-tools" "build-tools;28.0.3" "docs"
```

### Building

Get a fresh copy of the project

```bash
git clone https://github.com/Glusk2/sprouts.git
cd sprouts
```

The following command builds the project and runs all tests

```bash
chmod +x gradlew
./gradlew build connectedCheck
```

If you want to build one of the distributables (`.apk` for Android, `.jar` for
desktop), you have to run additional commands.

### Building distributables

-   Android

    ```bash
    ./gradlew android:assembleDebug
    ```

    The debug `.apk` is generated in `android/build/outputs/apk/debug/`

-   Desktop

    ```bash
    ./gradlew desktop:dist
    ```

    The executable `.jar` is generated in `desktop/build/libs/`

-   HTML

    ```bash
    ./gradlew html:dist
    ```

    The static web content (JS + HTML + CSS) files are generated in `/html/build/dist/`

-   iOS

    Todo!

## Documentation

The Javadoc for the latest build of the `master` branch is readily available
[here](https://glusk.github.io/sprouts/).

## Releases

Releasing new version is fully automated. To initiate a new release, simply post a
new comment to any issue with the following format: 

>Github, please release version: `<tag>`

where `<tag>` matches the following regular expression: `^v[0-9]+.[0-9]+.[0-9]+`
## IDE integration

You can find help on how to setup various IDEs for `libGDX` on their official
[page](https://libgdx.badlogicgames.com/documentation/gettingstarted/Setting%20Up.html).
