package com.example.westcon.fragments

import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.OnboardingScreen

class OnboardingFragment : BaseFragment() {
    @Composable
    override fun ScreenContent() {
        OnboardingScreen(
            onFinish = { navigateTo(LandingFragment()) }
        )
    }
}
