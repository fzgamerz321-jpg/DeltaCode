package com.deltacode.workspace.data.model

/**
 * Represents a workspace project entry displayed on the dashboard.
 *
 * @property id Unique identifier for the project.
 * @property name Display name of the project.
 * @property path Absolute path to the project root directory.
 * @property lastOpened Epoch milliseconds of the last time this project was opened.
 * @property sizeBytes Total size of the project directory in bytes.
 * @property language Primary programming language detected in the project.
 */
data class Project(
    val id: String,
    val name: String,
    val path: String,
    val lastOpened: Long,
    val sizeBytes: Long,
    val language: String = ""
)
