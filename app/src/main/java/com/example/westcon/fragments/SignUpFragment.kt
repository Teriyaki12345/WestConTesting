package com.example.westcon.fragments

import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.RegisterScreen

class SignUpFragment : BaseFragment() {
    @Composable
    override fun ScreenContent() {
        RegisterScreen(
            onJoinClick = { email, password ->
                navigateTo(SignUpStepTwoFragment.newInstance(email, password))
            },
            onBackClick = { parentFragmentManager.popBackStack() }
        )
    }
}
