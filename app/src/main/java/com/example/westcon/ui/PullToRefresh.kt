package com.example.westcon.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.westcon.ui.theme.WestconDarkBlue

/**
 * A reusable Pull-to-Refresh wrapper for Westcon app screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WestconPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val state = androidx.compose.material3.pulltorefresh.rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = state,
        modifier = modifier.fillMaxWidth(),
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = state,
                isRefreshing = isRefreshing,
                containerColor = Color.White,
                color = WestconDarkBlue,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        content()
    }
}
