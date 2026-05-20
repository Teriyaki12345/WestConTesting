package com.example.westcon.fragments

import android.os.Bundle
import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.SignUpStepTwoScreen

class SignUpStepTwoFragment : BaseFragment() {
    
    companion object {
        private const val ARG_EMAIL = "email"
        private const val ARG_PASSWORD = "password"

        fun newInstance(email: String, password: String): SignUpStepTwoFragment {
            val fragment = SignUpStepTwoFragment()
            val args = Bundle()
            args.putString(ARG_EMAIL, email)
            args.putString(ARG_PASSWORD, password)
            fragment.arguments = args
            return fragment
        }
    }

    @Composable
    override fun ScreenContent() {
        val email = arguments?.getString(ARG_EMAIL) ?: ""
        val password = arguments?.getString(ARG_PASSWORD) ?: ""
        
        SignUpStepTwoScreen(
            email = email,
            password = password,
            onNextClick = { navigateTo(DashboardFragment()) }
        )
    }
}
