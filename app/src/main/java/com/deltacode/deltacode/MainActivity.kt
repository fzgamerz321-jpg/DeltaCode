package com.deltacode.deltacode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.deltacode.deltacode.data.storage.WorkspaceManager
import com.deltacode.deltacode.ui.dashboard.DashboardScreen
import com.deltacode.deltacode.ui.dashboard.DashboardViewModel
import com.deltacode.deltacode.ui.theme.DeltaCodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val workspaceManager = WorkspaceManager(applicationContext)
        val viewModel = DashboardViewModel(workspaceManager)

        setContent {
            DeltaCodeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToEditor = { path ->
                            android.widget.Toast.makeText(
                                this,
                                "Open Workspace: $path",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            }
        }
    }
}