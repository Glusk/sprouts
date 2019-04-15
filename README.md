[![Codacy Badge](https://api.codacy.com/project/badge/Grade/239591577a9a4a83ab80bbd2f6fcfdea)](https://app.codacy.com/app/Glusk2/sprouts?utm_source=github.com&utm_medium=referral&utm_content=Glusk2/sprouts&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.com/Glusk2/sprouts.svg?branch=master)](https://travis-ci.com/Glusk2/sprouts)
[![Build status](https://ci.appveyor.com/api/projects/status/8p0xficdfjn90odb/branch/master?svg=true)](https://ci.appveyor.com/project/Glusk2/sprouts/branch/master)

# Compiling
Prerequisites:
- Java 8
- [Android SDK](https://developer.android.com/studio/#command-tools)

Update the `PATH` *environment variable* with the JDK `bin` folder
([help](https://stackoverflow.com/questions/1672281/environment-variables-for-java-installation)).

Unzip Android SDK and set `ANDROID_HOME` to the location of unzipped files.

Run this from the command line:

``` bash
# On Windows
# cd %ANDROID_HOME%/tools/bin
# sdkmanager "platforms;android-27" "platform-tools" "build-tools;27.0.3"

# On Unix
cd ANDROID_HOME/tools/bin
./sdkmanager "platforms;android-27" "platform-tools" "build-tools;27.0.3"

```

Now you are ready to clone...
```
git clone https://github.com/Glusk2/sprouts.git
cd sprouts
```
...and build the project:
``` bash
# On Windows
# gradlew build

# On Unix
chmod +x gradlew 
./gradlew build
```

Refer to [`.travis.yml`](https://github.com/Glusk2/sprouts/blob/master/.travis.yml)
script to target specific builds (see `before_deploy`, `deploy`).

# IDE integration

You can find help on how to setup various IDEs for `libGDX` on their official
[page](https://libgdx.badlogicgames.com/documentation/gettingstarted/Setting%20Up.html).
