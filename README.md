
<p>
 <img src="https://inspetor-assets.s3-sa-east-1.amazonaws.com/images/inspetor-logo.png" width="200" height="40" alt="Inspetor Logo">
</p>

# Inspetor Antifraud
Inspetor antifraud SDK for Android.

[ ![Download](https://api.bintray.com/packages/theosato/inspetor-android/inspetor/images/download.svg) ](https://bintray.com/theosato/inspetor-android/inspetor/_latestVersion)

## Description
Inspetor is a product developed to help your company avoid fraudulent transactions. This README file should help you to integrate the Inspetor Android library into your product. 

P.S.: This library was made in Kotlin and all of the code you'll see here is in Kotlin as well.

## Demo
If you want to see a very simple integration of the library in action you can clone the [inspetor demo app](https://github.com/inspetor/inspetor-android-demo-app). There you find an implementation on how to setup the library and trigger all tracking actions. Apart from that, you find the best practices when using our library.

## How to use
The Inspetor Android Library can be installed through [Nexus](https://oss.sonatype.org). To install all you have to do is follow this steps:

1. Add this line into your root `build.gradle` (**Project**) at the end of repositories (if it's not there yet):
```
allprojects {
  repositories {
    ...
    jcenter()
  }
}
```
2. Add this dependency to your application `build.grandle` (**Module.app**). Remember to change the version to the last one:
```
dependencies {
  ...
  implementation 'com.github.inspetor:inspetor:[version]@aar', { transitive = true }
  (e.g. 'com.github.inspetor:inspetor:1.2.1@aar')
}
```

The **transitive** statement says that the library will be imported with its dependencies. It's really important, so ***don't forget !***

## API Docs
You can find more in-depth documentation about our frontend libraries and integrations in general [here](https://inspetor.github.io/docs-frontend).

### Library setup
In order to properly relay information to Inspetor's processing pipeline, you'll need to provide customer-specific authentication credentials:
- App ID (provided by Inspetor)
- Tracker name (provided by Inspetor)

With these, you can instantiate the Inspetor tracking instance. Our integration library instantiates a singleton instance to prevent multiple trackers from being instantiated, which could otherwise result in duplicate or inconsistent data being relayed to Inspetor. Apart from that, the singleton will let you configure the library only once.

The singleton instance is instantiated as follows:

```
try {
    Inspetor.sharedInstance().setup(appId="appId", trackerName="trackerName", devEnv=false)
} catch (ex: Exception) {
    when (ex) {
        is InvalidCredentials -> { print("Error: $ex") }
        is ContextNotSetup -> { Inspetor.sharedInstance().setContext(context=applicationContext) }
    }
}
```

Be advised, that this function can throw two types of exceptions:

1. `InvalidCredentials` -> If you pass an invalid appId or/and trackerName (empty strings or not in the format required).
2. `ContextNotSetup` -> The Inspetor Android Library will try to get the `ApplicationContext` automatically on setup, but if this does not work you will need to manually pass the context to the library by using the `setContext` function

We **strongly** recommend you instantiate the Inspetor Library in your application `onCreate` function, since this way you will configure the library as soon as the app loads enabling you to call the library functions.

All the access to the Inspetor functions is made by calling the `Inspetor.sharedInstance()`. 

The parameters passed are the following, in order:

Parameter | Required | Type | Description 
--------- | -------- | ---- | ----------- 
appId | Yes | String | An unique identifier that the Inspetor Team will provide to you
trackerName | Yes | String | A name that will help us to find your data in our database and we'll provide you with a couple of them
devEnv | No | Boolean | Indicates that you are testing the library (development environment), meaning that data is not ready for production. All boolean parameters are `false` by default.

P.S: always remember to import the library using the `import com.inspetor.Inspetor`

### Library Calls
If you've already read the [general Inspetor files](https://inspetor.github.io/docs-frontend), you should be aware of all of Inspetor requests and collection functions.

Here we will show you some details to be aware of if you are calling the Inspetor tracking functions.

All of out *track functions* can throw exceptions, but the only exception they will through is if you forget to configure the Inspetor Library before calling one of them. Because of that, the Inspetor class has a function called `isConfigured()` that returns a boolean saying if you have configured or not the Inspetor Library. We recommend that when you call any of our tracking functions you check if the Inspetor Library is configured. Here is an example on how to do that:

```
if (Inspetor.sharedInstance().isConfigured()) {
    Inspetor.sharedInstance().trackAccountCreation(accountId="123")
}
```

#### TrackScreenView
Different from the Inspetor Javascript Library the track of user pageviews (screenview) is not done automatically. You need to add the function `trackPageView` on every new page.
We **strongly recommend** that you add the functions inside the `onCreate` function of every file associated with a `contentView` in your app, since this way we can track the pageview/screenview action as soon as it happens. You can see an example of this implementation bellow:

```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (Inspetor.sharedInstance().isConfigured()) {
        Inspetor.sharedInstance().trackPageView(pageTitle="login-page")
    }
}
```

### User Location
The Inspetor Android Library can use the user location to help us provide more accurate results, but it will **never** ask for it. If your app already has access to user location the library will automatically capture it, otherwise, it won't send the location to us.

### Models
If you are coming from one of our backend libraries you will notice that we do not use models (e.g. Account, Sale) in our frontend libraries. Here you just need to send us the id of the model (e.g. sale ID, account ID).

## More Information
For more info, you should check the [Inspetor Frontend docs](https://inspetor.github.io/docs-frontend)
