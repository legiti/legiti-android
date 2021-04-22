# Legiti Antifraud Android Library

## Setup
**To setup this library you will need to install AndroidStudio.**

Once you install AndroidStudio you can just clone this repository and this is all you need.

## How to make changes to the library
Just open this repository in AndroidStudio.

## How to test
There are 2 different tests in this repository:

### 1. Unit Test
1. You just need to run the `LegitiAndroidTest`. It should be the default target so you can just press `Shift+F10`

### 2. Integration Test / Test App
To run the integration test you need to deploy a new version of the library and test it in the https://github.com/legiti/legiti-android-demo-app/ app:
1. Update the version of the library in `Legiti/deploy.gradle` (should be in line 8). We recommend adding a `beta` to the version so it would look like `1.4.2-beta`
1. Publish a new version of our lib (check the "How to publish" part)
1. Open the `legiti-android-demo-app` in AndroidStudio
1. Update the version of the library in the `build.gradle` file (should be in line 36)
1. Re-sync Gradle (You can find the icon on the top right corner)
1. Run the app
1. Inside the app you can trigger the desired tracking actions
1. Check if data corresponding to the events you've triggered appear in the `sessions table` in the staging DB (You can use the `tracker_name` `legiti.test` to help you find the results)

### Sending data to staging
If you want to send data to the staging database you need to set the `LegitiDevEnv` as `true` when initializing the library. Here is an example:
```
Legiti.sharedInstance().setup(authToken, true)
```

## How to publish
To publish a new version of the Legiti Android Library you need to follow this steps:
1. Update the version of the library in `Legiti/build.gradle` (should be in line 86)
1. Add the sonatype configs to `local.properties` (in the root of the project). You can find all configs in 1password "Android Lib Local Properties"
1. Download the GPG Key (you can find it in 1password "Android Lib GPG File") in the root of the project
1. Inside the repository run the `make publish`

## Snowplow releases
https://github.com/snowplow/snowplow-android-tracker/releases