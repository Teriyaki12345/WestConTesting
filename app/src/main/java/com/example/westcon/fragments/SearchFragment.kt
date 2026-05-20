package com.example.westcon.fragments

import androidx.compose.runtime.Composable
import com.example.westcon.ui.screens.SearchScreen

class SearchFragment : BaseFragment() {
    @Composable
    override fun ScreenContent() {
        SearchScreen(
            onBackClick = { parentFragmentManager.popBackStack() }
        )
    }
}
