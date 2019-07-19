
<p>
  <img src="https://github.com/inspetor/slate/blob/master/source/images/logo-color.png" width="200" height="40" alt="Inspetor Logo"> </img>
</p>

# Inspetor Antifraud
Antrifraud Inspetor library for Android.

[ ![Download](https://api.bintray.com/packages/theosato/inspetor-android/inspetor/images/download.svg) ](https://bintray.com/theosato/inspetor-android/inspetor/_latestVersion)

## Description
Inspetor is an product developed to help your company to avoid fraudulent transactions. This READ ME file should help you to integrate the Inspetor PHP library into your product with a couple steps. 

P.S.: the library was made in Kotlin and all of the code you'll see here is Kotlin as well.

## Setup Guide
This is the step-by-step Inspetor integration:

### Library Repositories
When we build an Android library, the most professional way of publish it to multiple clients is upload the library into some known libraries repository as ***Jitpack***, ***JCenter***,  and ***Nexus***. Our stable versions will be into Nexus. Doing that, it's extremely easy to import our library into your project. Take a look in these 2 steps: 
1) Add this line into your root build.gradle at the end of repositories (if it's not there yet):
```
allprojects {
  repositories {
      ...
    	mavenCentral()
    }
}
```
The "mavenCentral" call says that your project will be able to use libraries hosted into Nexus repository.


2) Then add this dependency:
```
dependencies {
  ...
  implementation 'com.github.inspetor:inspetor:[version]@aar', { transitive = true }
  (e.g. 'com.github.inspetor:inspetor:1.2.1@aar')
}
 ```
It means you're importing our Inspetor arctifact from Nexus repo into your project. The **transitive** statement says that the library will be imported with it dependencies. It's really important, so ***don't forget it**!

With these 2 steps, you're already able to use our nice code! Let's try? 

### Library setup
You must provide your config to setup our library. To do that, we created a data class called InspetorConfig (duh) and you just have to do something like that:

```
  var config = InspetorConfig(trackerName, appId, devEnv)
  (e.g. InspetorConfig("cool.name", "ID123", true))
```

The ***"appId"*** is an unique identifier that the awesome Inspetor Team will provide you when you start to pay us. The ***"trackerName"*** is a name that will help us to find your data in our database and we'll provide you a couple of them. The ***"devEnv"*** is a boolean statement that you set to say if you want to use the develoment environment or prod. It's false by default. Okay, if you did everything right until now, you're really able to call our functions and to begin your fight against fraudulent transactions with us.

The whole code to instantiate our lib is gonna be like:
```
  val inspetor = InspetorClient()
  inspetor.setup(InspetorConfig("inspetor.test", "123", true))
  inspetor.collect(this)
```
After that, you can use all of our tracking methods without errors. We have a section to explain a little more of each function you'll see here, but don't worry too much right now. These are our first steps together, right?

Ok, but we'll start to code for real now, so we **strongly** recommend you to create an InspetorManager object (*like a **Singleton***). We trully believe that it's better if you call our library as a singleton to avoid instantiate the same class and trackers many times per user. Confusing? Relax, we're kind enough to show you how to do it.

```
  package com.android.yourapplication

  import android.content.Context
  import com.inspetor.*

  object InspetorManager {
      var config = InspetorConfig("inspetor.android", "1234", true)
      var inspetor: InspetorClient = InspetorClient()

      fun setup(context: Context) {
          inspetor.setup(config)
          inspetor.collect(context)
      }

      fun getClient(): InspetorClient {
          return this.inspetor
      }
  }
```

Now, wherever you need to call some Inspetor function, you just need to import this Class, get the client and _voilà_.

### Library Calls

I'm supposing you did an amazing job until this moment, so let's move on. It's time to make some calls and track some data. Nice, huh? Here we go.

If you've already read the [general Inspetor files](https://inspetor.github.io/slate/#introduction), you should be aware of all of Inspetor requests and trackers, so our intention here is just to show you how to use the PHP version of some of them.

Let's imagine that you want to put a tracker in your *"create transaction"* flow to send some data that the best Antifraud team should analyze and tell you if it's a fraud or not. So, it's intuitive that you need to call the *inspetorSaleCreation* and pass the data of that sale, right?

Yeah, but we must ask you a little favor. Considering the fraud context, it's possible that not all of your transaction data
help us to indenfity fraud, so we created a **Model** for each instance we use (remember what is a Model [here](https://inspetor.github.io/slate/#models)) that you must build and fill with it's needed. Let's see a snippet.

```
<?php

namespace NiceCompany\SaleFolder;

use NiceCompany\Inspetor\InspetorClass;

class Sale {
  ...

  public function someCompanyFunction() {
      // $company_sale is an example object of the company with sale data
      $inspetor_sale = $this->inspetorSaleBuilder($company_sale);

      $this->inspetor = new InspetorClass();

      if($inspetor_sale) {
          $inspetor->getClient()->trackSaleCreation($inspetor_sale);
      }
  }

  public function inspetorSaleBuilder($company_sale) {
      $model = $this->inspetor->getClient()->getInspetorSale();

      $model->setId($company_sale->getId());
      $model->setAccountId($company_sale->getUserId());
      $model->setStatus($company_sale->getSaleStatus());
      $model->setIsFraud($company_sale->getFraud());
      $model->set...

      return $model;
  }
  ...
}

?>
```

Following this code and assuming you've builded your model with all required parametes (find out each Model's required parameters [here](https://inspetor.github.io/slate/#models)), we *someCompanyFunction* run, the Inspetor code inside will send a great object with all we need to know about that sale. Easy?

We're using an auxiliar function *inspetorSaleBuilder* to build the *Sale Model* but you don't have to do it, or place it where we do here neither. You could set this *inspetorSaleBuilder* inside your *InspetorClass* that we talked about some lines above, for example. More tips in the section Best Practices & Tips.

### Models

The last snipped was a simple example to show how you should call our library and build one of our models. But now we're gonna talk about all of our Models, hoping you understand that some of them are not tracked it self but it's needed inside others. Take a look!

### What you should notice


### Conclusion
WOW! It was lovely to work with you, my friend. We trully hope that our instructions were clear and effective. Again, please tell us if we could make something better and contact us [here]().

Now you're invited to join our army against fraud 'cause ***STEALING IS BULLSHIT***!

*DPCL (dope cool)*
