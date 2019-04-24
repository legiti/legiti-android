# inspetor-android

[![](https://jitpack.io/v/theosato/inspetor-android.svg)](https://jitpack.io/#theosato/inspetor-android)

Kotlin framework for incorporating Inspetor antifraud monitoring into Android applications

## To Run
Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
		maven { url 'https://jitpack.io' }
	}
}
```

 Add these dependencies:
```
dependencies {
  implementation 'com.github.inspetor:inspetor-android:v1.0'
  implementation 'com.snowplowanalytics:snowplow-android-tracker:1.0.0@aar'
  implementation 'com.squareup.okhttp3:okhttp:3.4.1'
}
 ```
