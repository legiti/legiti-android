 
<p>
  <img src="https://github.com/inspetor/slate/blob/master/source/images/logo-color.png" width="200" height="40" alt="Inspetor Logo"> </img> 
</p>

# Inspetor Antifraud
Antrifraud Inspetor library for Android. 

## Description
This READ ME file is special! It should help you, my dear developer, on your "*publishing library road*". Let's chat about library development and publishing new versions 'cause the war against fraud never ends.

## Setup Guide
This is a step-by-step Inspetor publication guide:

### Development
Ok, so you decided to make changes in this beautiful library. No worries, I appreciate that.
Well, you should clone this repo:
```
git clone https://github.com/inspetor/inspetor-android.git
```
And when it's done, you can start to code. Every useful information to the development of this library can be found [here](https://github.com/inspetor/inspetor-android/blob/master/README.md), general information about Inspetor [here](https://inspetor.github.io/slate/) and [here](https://github.io/inspetor/libraries) are some libraries definitions (swagger).

### Publishing
Are you done? Nice! It's time to publish.

#### Realeases
The way to keep all active versions alive is creating a new release branch to push this new version.
```
git checkout -b release/[new version] (e.g release/1.2.3-beta)
```
Now, go back to your code and search the file: inspetor/deploy.gradle

This file is a script that upload your version into Bintray, the first library repository that we use in our deploys. 
To run the script, just run this command in your terminal:
```
./gradlew bintrayUpload
```
In a minute you can check your version uploaded [here](https://bintray.com/theosato/inspetor-android). 
With that, you're already able to import that new version, but we believe that we should use another repo too. That's why, after publish into bintray, we should just click the "Sync with Maven Central" button and, with the right OSS credentials, your version will be uploaded into Nexus (MavenCentral) repository.

And that's it. 

### Conclusion
Easy, huh? I hope this small guide has been helpful to anyone who wants to improve our library. Nice job!

And never forget that **STEALING IS BULLSHIT**. 

*DPCL (dope cool)*
