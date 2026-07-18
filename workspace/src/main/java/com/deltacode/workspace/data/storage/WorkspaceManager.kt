package com.deltacode.workspace.data.storage

import android.content.Context
import com.deltacode.workspace.data.model.Project
import com.deltacode.workspace.data.model.SortOrder
import org.json.JSONObject
import java.io.File
import java.util.UUID

/**
 * Manages project workspace directories under application internal storage.
 *
 * Each project lives in its own directory and contains a hidden `.delta`
 * folder with a `project.json` metadata file for session state, cursor
 * positions, and project configuration.
 */
class WorkspaceManager(private val context: Context) {

    private val workspacesRoot: File
        get() = File(context.filesDir, WORKSPACES_DIR).also { it.mkdirs() }

    /**
     * Creates a new project with the given [name].
     *
     * @return The newly created [Project].
     */
    fun createProject(name: String): Project {
        val id = UUID.randomUUID().toString()
        val projectDir = File(workspacesRoot, id)
        projectDir.mkdirs()

        val deltaDir = File(projectDir, DELTA_DIR)
        deltaDir.mkdirs()

        val project = Project(
            id = id,
            name = name,
            path = projectDir.absolutePath,
            lastOpened = System.currentTimeMillis(),
            sizeBytes = 0L
        )

        saveProjectMetadata(deltaDir, project)
        return project
    }

    /**
     * Returns all projects sorted by [sortOrder].
     */
    fun listProjects(sortOrder: SortOrder = SortOrder.TIME): List<Project> {
        val root = workspacesRoot
        if (!root.exists()) return emptyList()

        val projects = root.listFiles()
            ?.filter { it.isDirectory }
            ?.mapNotNull { dir -> loadProjectMetadata(dir) }
            ?: emptyList()

        return when (sortOrder) {
            SortOrder.TIME -> projects.sortedByDescending { it.lastOpened }
            SortOrder.NAME -> projects.sortedBy { it.name.lowercase() }
            SortOrder.SIZE -> projects.sortedByDescending { it.sizeBytes }
        }
    }

    /**
     * Deletes the project with the given [projectId].
     *
     * @return `true` if deletion was successful.
     */
    fun deleteProject(projectId: String): Boolean {
        val projectDir = File(workspacesRoot, projectId)
        return if (projectDir.exists()) {
            projectDir.deleteRecursively()
        } else {
            false
        }
    }

    /**
     * Renames the project with [projectId] to [newName].
     *
     * @return The updated [Project], or `null` if not found.
     */
    fun renameProject(projectId: String, newName: String): Project? {
        val projectDir = File(workspacesRoot, projectId)
        val deltaDir = File(projectDir, DELTA_DIR)
        val existing = loadProjectMetadata(projectDir) ?: return null

        val updated = existing.copy(name = newName)
        saveProjectMetadata(deltaDir, updated)
        return updated
    }

    /**
     * Updates the last-opened timestamp for the project.
     */
    fun touchProject(projectId: String) {
        val projectDir = File(workspacesRoot, projectId)
        val deltaDir = File(projectDir, DELTA_DIR)
        val existing = loadProjectMetadata(projectDir) ?: return

        val updated = existing.copy(lastOpened = System.currentTimeMillis())
        saveProjectMetadata(deltaDir, updated)
    }

    // ── Private helpers ────────────────────────────────────────────

    private fun saveProjectMetadata(deltaDir: File, project: Project) {
        deltaDir.mkdirs()
        val json = JSONObject().apply {
            put(KEY_ID, project.id)
            put(KEY_NAME, project.name)
            put(KEY_PATH, project.path)
            put(KEY_LAST_OPENED, project.lastOpened)
            put(KEY_SIZE_BYTES, project.sizeBytes)
            put(KEY_LANGUAGE, project.language)
        }
        File(deltaDir, METADATA_FILE).writeText(json.toString(2))
    }

    private fun loadProjectMetadata(projectDir: File): Project? {
        val metadataFile = File(projectDir, "$DELTA_DIR/$METADATA_FILE")
        if (!metadataFile.exists()) return null

        return try {
            val json = JSONObject(metadataFile.readText())
            val dirSize = calculateDirSize(projectDir)
            Project(
                id = json.getString(KEY_ID),
                name = json.getString(KEY_NAME),
                path = json.getString(KEY_PATH),
                lastOpened = json.getLong(KEY_LAST_OPENED),
                sizeBytes = dirSize,
                language = json.optString(KEY_LANGUAGE, "")
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateDirSize(dir: File): Long {
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    companion object {
        private const val WORKSPACES_DIR = "workspaces"
        private const val DELTA_DIR = ".delta"
        private const val METADATA_FILE = "project.json"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_PATH = "path"
        private const val KEY_LAST_OPENED = "lastOpened"
        private const val KEY_SIZE_BYTES = "sizeBytes"
        private const val KEY_LANGUAGE = "language"
    }
}
