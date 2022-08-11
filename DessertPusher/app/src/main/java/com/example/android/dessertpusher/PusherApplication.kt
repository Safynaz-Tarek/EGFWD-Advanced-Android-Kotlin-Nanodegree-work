package com.example.android.dessertpusher
import android.app.Application
import timber.log.Timber
//An application class is a base class that contains global app state for your entire app
//It's also the main object that the OS uses to interact with the app
//There is a default application class that is used by the OS

//When we want to do some specific setup for a utility that the entire app is going to use
//For example -> Timber
//Timber needs to use the application class bc the whole app will be using this logging library
//Also we wanna initialize timber before anything else is initialized
//So we make a subclass of the application & override the defaults with our own implementation
class PusherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }
}