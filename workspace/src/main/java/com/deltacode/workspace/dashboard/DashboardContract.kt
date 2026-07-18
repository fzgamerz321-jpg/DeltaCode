package com.deltacode.workspace.dashboard

import com.deltacode.workspace.data.model.DashboardTab
import com.deltacode.workspace.data.model.Project
import com.deltacode.workspace.data.model.SortOrder

/**
 * MVI contract for the Dashboard screen.
 */
object DashboardContract {

    /**
     * Immutable UI state for the dashboard.
     */
    data class State(
        val projects: List<Project> = emptyList(),
        val sortOrder: SortOrder = SortOrder.TIME,
        val selectedTab: DashboardTab = DashboardTab.PROJECTS,
        val isLoading: Boolean = false
    )

    /**
     * User-initiated events on the dashboard.
     */
    sealed class Event {
        data class SelectTab(val tab: DashboardTab) : Event()
        data class ChangeSort(val sortOrder: SortOrder) : Event()
        data class CreateProject(val name: String) : Event()
        data class DeleteProject(val projectId: String) : Event()
        data class OpenProject(val project: Project) : Event()
        data object LoadProjects : Event()
    }

    /**
     * One-shot side effects emitted by the ViewModel.
     */
    sealed class Effect {
        data class NavigateToEditor(val project: Project) : Effect()
        data class ShowError(val message: String) : Effect()
    }
}
