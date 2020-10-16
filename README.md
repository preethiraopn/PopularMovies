# Axonista Test App

### The App is developed using core android architechture components and purely follows the android MVVM pattern,

##### I have referred Android github sample project to develop this app

[Sample App](https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample)

#### Core components
* **LiveData**
* **DataBinding**
* **Room database**
* **Viewmodel**
* **Lifecycle aware components**
* **Coroutines**
* **Dagger2**

#### Strictly following the Architechture

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

#### Core Libraries used

* **Retrofit - Network Related operations**
* **Dagger - For Dependency Injection**
* **Glide - For Image loading and caching**


#### Test Cases

#### I have wrriten both Unit tests and Android UI tests

##### The libraries I have used for test cases are 

* **Mockito for Unit testing along with some android built in libraries**
* **Espresso for Android UI Testing along with some android built in libraries.**
* **Mockito for Kotlin**

#### Accomplished Features:
```
- The minimum SDK supported is API 19.
- Supported in both landscape and potrait mode.
- The app is written in Kotlin programming language.
- Added unit and UI tests.
- Loading MovieList with Pagination Support.
- Strictly following MVVM Architecture Pattern with writing comments for all the core functionalites.
- Added offline caching support using retrofit lirabry <br>&#x25CF; offline online notification to the application.
```

#### Room For Improvements
* **Could have implemented Room Database for offline storage(right refrofit doing the job)**
* **storing securely the API howerver its is not a queite good approach to store API key locally, but we can go ahead and store it in native part(C, C++) because of time constraint I wasnt able to do this part(I have done it in my previous projects)**
* **Could have enabled a proguard (time constraint but I do this to all my applications)** 

#### The Challeging part
* **This time I used latest Dagger-2 Version , which had some new things understanding them and fixing them was quite tedious(less online support).**
* **Kotlin with mockito unit testing is an challening part**
