package com.deltacode.deltacode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.deltacode.component.theme.DeltaCodeTheme
import com.deltacode.workspace.dashboard.DashboardScreen
import com.deltacode.workspace.dashboard.DashboardViewModel

/**
 * Application entry point.
 *
 * Launches the Home Dashboard as the primary screen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        setContent {
            DeltaCodeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DashboardScreen(viewModel = viewModel)
                }
            }
        }
    }
}