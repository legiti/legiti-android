# Antifraud Inspetor Android Library

[ ![Download](https://api.bintray.com/packages/theosato/inspetor-android/inspetor/images/download.svg) ](https://bintray.com/theosato/inspetor-android/inspetor/_latestVersion)

## Overview
Kotlin framework for incorporating Inspetor antifraud monitoring into Android applications.
With that, it's possible to track some kind of events of Android applications.

## To Run
Add it in your root build.gradle at the end of repositories (if it's not there yet):
```
allprojects {
  repositories {
      ...
    	mavenCentral()
    }
}
```

 Add these dependencies:
```
dependencies {
  ...
  implementation 'com.github.inspetor:inspetor:0.0.1@aar', { transitive = true }
}
 ```
