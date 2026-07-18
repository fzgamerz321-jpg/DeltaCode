package com.deltacode.workspace.data.model

/**
 * Sort options for the project list on the dashboard.
 */
enum class SortOrder {
    /** Sort by last opened time, most recent first. */
    TIME,
    /** Sort alphabetically by project name. */
    NAME,
    /** Sort by project size, largest first. */
    SIZE
}
