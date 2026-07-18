package com.deltacode.workspace.data.model

/**
 * Navigation tabs available on the home dashboard.
 */
enum class DashboardTab(val label: String) {
    /** Workspace projects listing and management. */
    PROJECTS("Projects"),
    /** AI provider configuration and model management. */
    AI_CONFIG("AI Config"),
    /** Application settings and preferences. */
    SETTINGS("Settings"),
    /** Language interpreter/runtime management. */
    INTERPRETER("Interpreter")
}
