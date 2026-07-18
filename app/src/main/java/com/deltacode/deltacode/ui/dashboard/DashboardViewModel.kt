package com.deltacode.deltacode.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deltacode.deltacode.data.model.AIProvider
import com.deltacode.deltacode.data.model.Project
import com.deltacode.deltacode.data.storage.WorkspaceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel orchestrating MVI data flows for DeltaCode Dashboard.
 */
class DashboardViewModel(private val workspaceManager: WorkspaceManager) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<DashboardEffect>()
    val effects: SharedFlow<DashboardEffect> = _effects.asSharedFlow()

    init {
        handleIntent(DashboardIntent.LoadData)
    }

    /**
     * Dispatch user intent to process states.
     */
    fun handleIntent(intent: DashboardIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is DashboardIntent.LoadData -> loadDashboardData()
                is DashboardIntent.CreateProject -> createProject(intent.name)
                is DashboardIntent.ImportProject -> importProject(intent.path)
                is DashboardIntent.ToggleArchive -> toggleArchive(intent.project)
                is DashboardIntent.DuplicateProject -> duplicateProject(intent.project)
                is DashboardIntent.RenameProject -> renameProject(intent.project, intent.newName)
                is DashboardIntent.DeleteProject -> deleteProject(intent.project)
                is DashboardIntent.UpdateAIProvider -> updateAIProvider(intent.provider)
                is DashboardIntent.OpenProject -> openProject(intent.project)
                is DashboardIntent.SetCreateDialogVisible -> {
                    _uiState.value = _uiState.value.copy(showCreateDialog = intent.visible)
                }
                is DashboardIntent.SetImportDialogVisible -> {
                    _uiState.value = _uiState.value.copy(showImportDialog = intent.visible)
                }
                is DashboardIntent.SetAIProviderDialogVisible -> {
                    _uiState.value = _uiState.value.copy(
                        showAIProviderDialog = intent.visible,
                        selectedAIProvider = intent.provider
                    )
                }
                is DashboardIntent.SetRenameProjectTarget -> {
                    _uiState.value = _uiState.value.copy(renamingProject = intent.project)
                }
                is DashboardIntent.ClearError -> {
                    _uiState.value = _uiState.value.copy(error = null)
                }
            }
        }
    }

    private fun loadDashboardData() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            val projects = workspaceManager.getProjects()
            val providers = workspaceManager.getAIProviders()
            _uiState.value = _uiState.value.copy(
                projects = projects,
                aiProviders = providers,
                isLoading = false
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Failed to load workspaces: ${e.localizedMessage}"
            )
        }
    }

    private suspend fun createProject(name: String) {
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Project name cannot be empty")
            return
        }
        try {
            val project = workspaceManager.createProject(name)
            loadDashboardData()
            _uiState.value = _uiState.value.copy(showCreateDialog = false)
            _effects.emit(DashboardEffect.ShowToast("Created project: $name"))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to create project: ${e.localizedMessage}")
        }
    }

    private suspend fun importProject(path: String) {
        if (path.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Path cannot be empty")
            return
        }
        try {
            val project = workspaceManager.importProject(path)
            loadDashboardData()
            _uiState.value = _uiState.value.copy(showImportDialog = false)
            _effects.emit(DashboardEffect.ShowToast("Imported project: ${project.name}"))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to import folder: ${e.localizedMessage}")
        }
    }

    private suspend fun toggleArchive(project: Project) {
        try {
            workspaceManager.archiveProject(project, !project.isArchived)
            loadDashboardData()
            val text = if (project.isArchived) "Unarchived" else "Archived"
            _effects.emit(DashboardEffect.ShowToast("$text project: ${project.name}"))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to update project status: ${e.localizedMessage}")
        }
    }

    private suspend fun duplicateProject(project: Project) {
        try {
            val duplicated = workspaceManager.duplicateProject(project)
            loadDashboardData()
            _effects.emit(DashboardEffect.ShowToast("Duplicated project to ${duplicated.name}"))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to duplicate project: ${e.localizedMessage}")
        }
    }

    private suspend fun renameProject(project: Project, newName: String) {
        if (newName.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "New name cannot be empty")
            return
        }
        try {
            workspaceManager.renameProject(project, newName)
            loadDashboardData()
            _uiState.value = _uiState.value.copy(renamingProject = null)
            _effects.emit(DashboardEffect.ShowToast("Renamed project to: $newName"))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to rename: ${e.localizedMessage}")
        }
    }

    private suspend fun deleteProject(project: Project) {
        try {
            workspaceManager.deleteProject(project)
            loadDashboardData()
            _effects.emit(DashboardEffect.ShowToast("Deleted project: ${project.name}"))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to delete: ${e.localizedMessage}")
        }
    }

    private suspend fun updateAIProvider(provider: AIProvider) {
        try {
            workspaceManager.updateAIProvider(provider)
            loadDashboardData()
            _uiState.value = _uiState.value.copy(showAIProviderDialog = false, selectedAIProvider = null)
            _effects.emit(DashboardEffect.ShowToast("Updated configuration for ${provider.name}"))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to update AI settings: ${e.localizedMessage}")
        }
    }

    private suspend fun openProject(project: Project) {
        try {
            workspaceManager.markProjectOpened(project)
            loadDashboardData()
            _effects.emit(DashboardEffect.NavigateToEditor(project.path))
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = "Failed to open project: ${e.localizedMessage}")
        }
    }
}
