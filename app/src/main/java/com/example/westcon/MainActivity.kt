package com.example.westcon

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.example.westcon.fragments.OnboardingFragment
import com.example.westcon.fragments.DashboardFragment
import com.example.westcon.data.FirebaseManager
import com.google.firebase.FirebaseApp

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase early so FirebaseAuth/Firestore can find the default app config.
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        // Makes the UI draw behind the status bar for that full-screen background effect
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val startFragment = if (FirebaseManager.isUserLoggedIn()) {
                DashboardFragment()
            } else {
                OnboardingFragment()
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container, startFragment)
            }
        }
    }
}
