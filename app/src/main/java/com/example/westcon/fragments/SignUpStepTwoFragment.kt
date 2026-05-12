package com.example.westcon.fragments

import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.SignUpStepTwoScreen

class SignUpStepTwoFragment : BaseFragment() {
    @Composable
    override fun ScreenContent() {
        SignUpStepTwoScreen(
            onNextClick = { navigateTo(DashboardFragment()) }
        )
    }
}
