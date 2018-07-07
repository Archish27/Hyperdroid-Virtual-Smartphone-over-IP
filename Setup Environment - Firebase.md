## Setting up Firebase

Log on to Google Firebase, Login using your credentials and visit the console page.https://console.firebase.google.com/

Create a new project, add necessary information to it and the services to opt for. Configuration of android app is easy using Android Studio’s inherent support for
Firebase assistant. The Assistant can connect your existing project or create a new one for you with all the necessary gradle dependencies.
If you're using an older version of Android Studio or have a more complex project configuration, you can still manually add Firebase to your app.

**Use the Firebase Assistant**

To open the Firebase Assistant in Android Studio

- Click ```Tools > Firebase``` to open the ```Assistant``` window.
        
- Click to expand one of the listed features (for example, Analytics), then click the provided tutorial link (for example, Log an Analytics event).

- Click the ```Connect to Firebase``` button to connect to Firebase and add the necessary code to your app.

**Manually add Firebase**

To add Firebase to your app you'll need a Firebase project and a Firebase configuration file for your app.

**1.** Create a Firebase project in the Firebase console, if you don't already have one. If you already have an existing Google project associated with your mobile app, click **Import Google Project**. Otherwise, click **Add project**.

**2.** Click **Add Firebase to your Android app** and follow the setup steps. If you're importing an existing Google project, this may happen automatically and you can just download the config file.

**3.** When prompted, enter your app's package name. It's important to enter the package name your app is using; this can only be set when you add an app to your Firebase project.

**4.** At the end, you'll download a ```google-services.json``` file. You can download this file again at any time.

**5.** If you haven't done so already, copy this into your project's module folder, typically ```app/```.
