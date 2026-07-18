package com.deltacode.deltacode.data.model

/**
 * Represents a workspace project in DeltaCode.
 */
data class Project(
    val name: String,
    val path: String,
    val lastOpened: Long,
    val isArchived: Boolean = false,
    val gitUrl: String? = null
)
