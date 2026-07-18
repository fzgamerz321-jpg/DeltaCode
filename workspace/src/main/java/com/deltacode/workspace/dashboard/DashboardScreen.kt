package com.deltacode.workspace.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.deltacode.component.DeltaNavCard
import com.deltacode.component.DeltaProjectCard
import com.deltacode.component.DeltaSortBar
import com.deltacode.component.DeltaTypingText
import com.deltacode.component.theme.AccentPrimary
import com.deltacode.component.theme.BgBase
import com.deltacode.component.theme.TextMuted
import com.deltacode.component.theme.TextPrimary
import com.deltacode.component.theme.TextSecondary
import com.deltacode.workspace.data.model.DashboardTab
import com.deltacode.workspace.data.model.SortOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Main dashboard screen composable.
 *
 * Assembles the welcome header, navigation row, sort bar,
 * and project list from reusable components.
 */
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        // ── Welcome Header ─────────────────────────────────────
        WelcomeHeader()

        Spacer(modifier = Modifier.height(20.dp))

        // ── Navigation Row ─────────────────────────────────────
        NavigationRow(
            selectedTab = state.selectedTab,
            onTabSelected = { viewModel.onEvent(DashboardContract.Event.SelectTab(it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Tab Content ────────────────────────────────────────
        when (state.selectedTab) {
            DashboardTab.PROJECTS -> ProjectsContent(
                state = state,
                onSortChanged = { viewModel.onEvent(DashboardContract.Event.ChangeSort(it)) },
                onProjectClick = { viewModel.onEvent(DashboardContract.Event.OpenProject(it)) },
                onProjectMenuClick = { /* TODO: show project options dialog */ }
            )
            DashboardTab.AI_CONFIG -> PlaceholderContent("AI Configuration")
            DashboardTab.SETTINGS -> PlaceholderContent("Settings")
            DashboardTab.INTERPRETER -> PlaceholderContent("Interpreter")
        }
    }
}

// ── Welcome Header ────────────────────────────────────────────────

@Composable
private fun WelcomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Welcome to",
            style = MaterialTheme.typography.headlineMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        DeltaTypingText(
            text = "Delta Code",
            style = MaterialTheme.typography.displayLarge,
            textColor = AccentPrimary,
            typingDelayMs = 100L
        )
    }
}

// ── Navigation Row ────────────────────────────────────────────────

private data class NavItem(
    val tab: DashboardTab,
    val icon: ImageVector
)

private val navItems = listOf(
    NavItem(DashboardTab.PROJECTS, Icons.Default.Code),
    NavItem(DashboardTab.AI_CONFIG, Icons.Default.SmartToy),
    NavItem(DashboardTab.SETTINGS, Icons.Default.Settings),
    NavItem(DashboardTab.INTERPRETER, Icons.Default.Build)
)

@Composable
private fun NavigationRow(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(navItems) { item ->
            DeltaNavCard(
                label = item.tab.label,
                icon = item.icon,
                isSelected = selectedTab == item.tab,
                onClick = { onTabSelected(item.tab) }
            )
        }
    }
}

// ── Projects Content ──────────────────────────────────────────────

private val sortLabels = listOf("Time", "Name", "Size")
private val sortValues = listOf(SortOrder.TIME, SortOrder.NAME, SortOrder.SIZE)

@Composable
private fun ProjectsContent(
    state: DashboardContract.State,
    onSortChanged: (SortOrder) -> Unit,
    onProjectClick: (com.deltacode.workspace.data.model.Project) -> Unit,
    onProjectMenuClick: (com.deltacode.workspace.data.model.Project) -> Unit
) {
    Column {
        // Sort Bar
        DeltaSortBar(
            options = sortLabels,
            selectedIndex = sortValues.indexOf(state.sortOrder),
            onSortSelected = { index -> onSortChanged(sortValues[index]) },
            onManageClick = { /* TODO: manage mode */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.projects.isEmpty()) {
            // Empty state
            EmptyProjectsState()
        } else {
            // Project list
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(
                    items = state.projects,
                    key = { it.id }
                ) { project ->
                    DeltaProjectCard(
                        name = project.name,
                        dateText = formatDate(project.lastOpened),
                        sizeText = formatSize(project.sizeBytes),
                        onClick = { onProjectClick(project) },
                        onMenuClick = { onProjectMenuClick(project) }
                    )
                }
            }
        }
    }
}

// ── Empty State ───────────────────────────────────────────────────

@Composable
private fun EmptyProjectsState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No projects yet",
                style = MaterialTheme.typography.titleMedium,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create a new project to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }
    }
}

// ── Placeholder Content ───────────────────────────────────────────

@Composable
private fun PlaceholderContent(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title — Coming Soon",
            style = MaterialTheme.typography.titleMedium,
            color = TextMuted
        )
    }
}

// ── Formatters ────────────────────────────────────────────────────

private val dateFormat = SimpleDateFormat("M/d/yyyy h:mm", Locale.getDefault())

private fun formatDate(epochMillis: Long): String {
    return dateFormat.format(Date(epochMillis))
}

private fun formatSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "${bytes}B"
        bytes < 1024 * 1024 -> "${"%.1f".format(bytes / 1024.0)}K"
        bytes < 1024 * 1024 * 1024 -> "${"%.1f".format(bytes / (1024.0 * 1024.0))}M"
        else -> "${"%.1f".format(bytes / (1024.0 * 1024.0 * 1024.0))}G"
    }
}
