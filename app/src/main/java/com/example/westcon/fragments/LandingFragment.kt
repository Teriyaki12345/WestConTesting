package com.example.westcon.fragments

import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.WestconLoginScreen

class LandingFragment : BaseFragment() {
    @Composable
    override fun ScreenContent() {
        WestconLoginScreen(
            onSignUpClick = { navigateTo(SignUpFragment()) },
            onLoginClick = { navigateTo(LoginFragment()) }
        )
    }
}
