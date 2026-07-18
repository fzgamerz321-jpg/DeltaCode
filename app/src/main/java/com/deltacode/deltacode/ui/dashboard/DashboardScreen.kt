package com.deltacode.deltacode.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deltacode.deltacode.data.model.AIProvider
import com.deltacode.deltacode.data.model.Project
import com.deltacode.deltacode.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToEditor: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is DashboardEffect.NavigateToEditor -> {
                    onNavigateToEditor(effect.projectPath)
                }
                is DashboardEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Main layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(BgElevated)
                    .border(1.dp, BorderDefault)
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "DeltaCode Logo",
                        tint = AccentPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "DeltaCode IDE",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "Settings opened", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(48.dp) // Accessibility hit targets
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Main Body Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Left Column: Recent Projects
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                        .background(BgElevated)
                        .border(1.dp, BorderDefault)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Recent Projects",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (state.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AccentPrimary)
                        }
                    } else if (state.projects.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No workspaces found",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.projects) { project ->
                                ProjectItemRow(
                                    project = project,
                                    onOpen = { viewModel.handleIntent(DashboardIntent.OpenProject(project)) },
                                    onRename = { viewModel.handleIntent(DashboardIntent.SetRenameProjectTarget(project)) },
                                    onDuplicate = { viewModel.handleIntent(DashboardIntent.DuplicateProject(project)) },
                                    onArchive = { viewModel.handleIntent(DashboardIntent.ToggleArchive(project)) },
                                    onDelete = { viewModel.handleIntent(DashboardIntent.DeleteProject(project)) }
                                )
                            }
                        }
                    }
                }

                // Right Column: Start / Templates / AI Control Panels
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Start Block
                    item {
                        Column {
                            Text(
                                text = "Start",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                IDEButton(
                                    text = "New Project",
                                    onClick = { viewModel.handleIntent(DashboardIntent.SetCreateDialogVisible(true)) },
                                    isPrimary = true,
                                    modifier = Modifier.weight(1f)
                                )
                                IDEButton(
                                    text = "Import Folder",
                                    onClick = { viewModel.handleIntent(DashboardIntent.SetImportDialogVisible(true)) },
                                    isPrimary = false,
                                    modifier = Modifier.weight(1f)
                                )
                                IDEButton(
                                    text = "Clone Repository",
                                    onClick = {
                                        Toast.makeText(context, "Git Clone setup coming soon!", Toast.LENGTH_SHORT).show()
                                    },
                                    isPrimary = false,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Project Templates Row
                    item {
                        Column {
                            Text(
                                text = "Project Templates",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            val templates = listOf(
                                Triple("Empty Project", "Create a blank workspace directory", "empty"),
                                Triple("Kotlin App", "Kotlin Console App scaffold project", "kotlin"),
                                Triple("Rust Lib", "Rust cargo library project config", "rust"),
                                Triple("Web Static", "Simple HTML / CSS / JS bundle", "web")
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(templates) { (name, desc, type) ->
                                    TemplateCard(
                                        name = name,
                                        description = desc,
                                        onClick = {
                                            viewModel.handleIntent(DashboardIntent.CreateProject("My_Template_$name"))
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // AI Assistant Settings List
                    item {
                        Column {
                            Text(
                                text = "AI Providers Setup",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                for (provider in state.aiProviders) {
                                    AIProviderRow(
                                        provider = provider,
                                        onConfigure = {
                                            viewModel.handleIntent(DashboardIntent.SetAIProviderDialogVisible(provider, true))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Overlay Windows & Dialog forms
        if (state.showCreateDialog) {
            CreateProjectDialog(
                onDismiss = { viewModel.handleIntent(DashboardIntent.SetCreateDialogVisible(false)) },
                onSubmit = { name -> viewModel.handleIntent(DashboardIntent.CreateProject(name)) }
            )
        }

        if (state.showImportDialog) {
            ImportFolderDialog(
                defaultPath = workspaceManagerPath(context),
                onDismiss = { viewModel.handleIntent(DashboardIntent.SetImportDialogVisible(false)) },
                onSubmit = { path -> viewModel.handleIntent(DashboardIntent.ImportProject(path)) }
            )
        }

        state.renamingProject?.let { project ->
            RenameProjectDialog(
                project = project,
                onDismiss = { viewModel.handleIntent(DashboardIntent.SetRenameProjectTarget(null)) },
                onSubmit = { newName -> viewModel.handleIntent(DashboardIntent.RenameProject(project, newName)) }
            )
        }

        if (state.showAIProviderDialog) {
            state.selectedAIProvider?.let { provider ->
                AIConfigDialog(
                    provider = provider,
                    onDismiss = { viewModel.handleIntent(DashboardIntent.SetAIProviderDialogVisible(null, false)) },
                    onSubmit = { updated -> viewModel.handleIntent(DashboardIntent.UpdateAIProvider(updated)) }
                )
            }
        }
    }
}

@Composable
fun ProjectItemRow(
    project: Project,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    onDuplicate: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BgSurface)
            .border(1.dp, BorderDefault, RoundedCornerShape(10.dp))
            .clickable { onOpen() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = project.name,
                        color = if (project.isArchived) TextDisabled else TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (project.isArchived) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(Archived)",
                            color = StateWarning,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = project.path,
                    color = TextMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Actions Menu",
                        tint = TextSecondary
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(BgElevated)
                ) {
                    DropdownMenuItem(
                        text = { Text("Open", color = TextPrimary) },
                        onClick = { expanded = false; onOpen() }
                    )
                    DropdownMenuItem(
                        text = { Text("Rename", color = TextPrimary) },
                        onClick = { expanded = false; onRename() }
                    )
                    DropdownMenuItem(
                        text = { Text("Duplicate", color = TextPrimary) },
                        onClick = { expanded = false; onDuplicate() }
                    )
                    DropdownMenuItem(
                        text = { Text(if (project.isArchived) "Unarchive" else "Archive", color = TextPrimary) },
                        onClick = { expanded = false; onArchive() }
                    )
                    Divider(color = BorderDefault)
                    DropdownMenuItem(
                        text = { Text("Delete", color = StateError) },
                        onClick = { expanded = false; onDelete() }
                    )
                }
            }
        }
    }
}

@Composable
fun TemplateCard(
    name: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, BorderDefault),
        colors = CardDefaults.cardColors(containerColor = BgSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Zip Template",
                tint = StateInfo,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = name,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AIProviderRow(
    provider: AIProvider,
    onConfigure: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(BgSurface)
            .border(1.dp, BorderDefault, RoundedCornerShape(10.dp))
            .clickable { onConfigure() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = if (provider.isEnabled) Icons.Default.Star else Icons.Default.Info,
                contentDescription = provider.name,
                tint = if (provider.isEnabled) StateSuccess else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = provider.name,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (provider.isEnabled) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(StateSuccess.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Active",
                                color = StateSuccess,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    text = "Model: ${provider.defaultModel} | API Key: ${if (provider.apiKey.isEmpty()) "Not entered" else "••••••••"}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        IconButton(
            onClick = onConfigure,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Settings",
                tint = TextSecondary
            )
        }
    }
}

private fun workspaceManagerPath(context: android.content.Context): String {
    // Utility for dialog defaults
    return context.filesDir.absolutePath
}
