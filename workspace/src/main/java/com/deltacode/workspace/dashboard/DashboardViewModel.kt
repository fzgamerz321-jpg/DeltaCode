package com.deltacode.workspace.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deltacode.workspace.data.model.DashboardTab
import com.deltacode.workspace.data.model.SortOrder
import com.deltacode.workspace.data.storage.WorkspaceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the dashboard screen following the MVI pattern.
 *
 * Processes [DashboardContract.Event]s, updates [DashboardContract.State],
 * and emits one-shot [DashboardContract.Effect]s.
 */
class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val workspaceManager = WorkspaceManager(application)

    private val _state = MutableStateFlow(DashboardContract.State())
    val state: StateFlow<DashboardContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardContract.Effect>()
    val effect: SharedFlow<DashboardContract.Effect> = _effect.asSharedFlow()

    init {
        onEvent(DashboardContract.Event.LoadProjects)
    }

    /**
     * Dispatches a user event for processing.
     */
    fun onEvent(event: DashboardContract.Event) {
        when (event) {
            is DashboardContract.Event.LoadProjects -> loadProjects()
            is DashboardContract.Event.SelectTab -> selectTab(event.tab)
            is DashboardContract.Event.ChangeSort -> changeSort(event.sortOrder)
            is DashboardContract.Event.CreateProject -> createProject(event.name)
            is DashboardContract.Event.DeleteProject -> deleteProject(event.projectId)
            is DashboardContract.Event.OpenProject -> openProject(event.project)
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val projects = withContext(Dispatchers.IO) {
                workspaceManager.listProjects(_state.value.sortOrder)
            }
            _state.update { it.copy(projects = projects, isLoading = false) }
        }
    }

    private fun selectTab(tab: DashboardTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    private fun changeSort(sortOrder: SortOrder) {
        _state.update { it.copy(sortOrder = sortOrder) }
        loadProjects()
    }

    private fun createProject(name: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    workspaceManager.createProject(name)
                }
                loadProjects()
            } catch (e: Exception) {
                _effect.emit(
                    DashboardContract.Effect.ShowError(
                        e.message ?: "Failed to create project"
                    )
                )
            }
        }
    }

    private fun deleteProject(projectId: String) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                workspaceManager.deleteProject(projectId)
            }
            if (success) {
                loadProjects()
            } else {
                _effect.emit(DashboardContract.Effect.ShowError("Failed to delete project"))
            }
        }
    }

    private fun openProject(project: com.deltacode.workspace.data.model.Project) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                workspaceManager.touchProject(project.id)
            }
            _effect.emit(DashboardContract.Effect.NavigateToEditor(project))
        }
    }
}
