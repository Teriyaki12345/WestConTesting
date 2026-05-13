package com.example.westcon

import android.app.Application
import com.google.firebase.FirebaseApp

class WestConApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }
}
