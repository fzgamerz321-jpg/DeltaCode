package com.deltacode.deltacode.ui.dashboard

import com.deltacode.deltacode.data.model.AIProvider
import com.deltacode.deltacode.data.model.Project

/**
 * UI State for the Dashboard screen.
 */
data class DashboardState(
    val projects: List<Project> = emptyList(),
    val aiProviders: List<AIProvider> = emptyList(),
    val isLoading: Boolean = false,
    val showCreateDialog: Boolean = false,
    val showImportDialog: Boolean = false,
    val showAIProviderDialog: Boolean = false,
    val selectedAIProvider: AIProvider? = null,
    val renamingProject: Project? = null,
    val error: String? = null
)

/**
 * UI Intents representing user actions.
 */
sealed interface DashboardIntent {
    object LoadData : DashboardIntent
    data class CreateProject(val name: String) : DashboardIntent
    data class ImportProject(val path: String) : DashboardIntent
    data class ToggleArchive(val project: Project) : DashboardIntent
    data class DuplicateProject(val project: Project) : DashboardIntent
    data class RenameProject(val project: Project, val newName: String) : DashboardIntent
    data class DeleteProject(val project: Project) : DashboardIntent
    data class UpdateAIProvider(val provider: AIProvider) : DashboardIntent
    data class OpenProject(val project: Project) : DashboardIntent
    
    // Dialog triggers
    data class SetCreateDialogVisible(val visible: Boolean) : DashboardIntent
    data class SetImportDialogVisible(val visible: Boolean) : DashboardIntent
    data class SetAIProviderDialogVisible(val provider: AIProvider?, val visible: Boolean) : DashboardIntent
    data class SetRenameProjectTarget(val project: Project?) : DashboardIntent
    object ClearError : DashboardIntent
}

/**
 * Side effects triggered by UI events.
 */
sealed interface DashboardEffect {
    data class NavigateToEditor(val projectPath: String) : DashboardEffect
    data class ShowToast(val message: String) : DashboardEffect
}
