package com.deltacode.deltacode.data.storage

import android.content.Context
import com.deltacode.deltacode.data.model.AIProvider
import com.deltacode.deltacode.data.model.Project
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException

/**
 * Manages workspaces, project directories, config storage, and AI providers.
 */
class WorkspaceManager(private val filesDir: java.io.File) {
    constructor(context: android.content.Context) : this(context.filesDir)

    private val projectsFile = File(filesDir, "projects.json")
    private val aiProvidersFile = File(filesDir, "ai_providers.json")
    private val defaultWorkspacesDir = File(filesDir, "workspaces").apply {
        if (!exists()) mkdirs()
    }


    /**
     * Get default workspaces directory path.
     */
    fun getDefaultWorkspacesDirPath(): String = defaultWorkspacesDir.absolutePath

    /**
     * Load recent projects list from global metadata.
     */
    fun getProjects(): List<Project> {
        if (!projectsFile.exists()) return emptyList()
        return try {
            val content = projectsFile.readText()
            val array = JSONArray(content)
            val list = mutableListOf<Project>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Project(
                        name = obj.getString("name"),
                        path = obj.getString("path"),
                        lastOpened = obj.getLong("lastOpened"),
                        isArchived = obj.optBoolean("isArchived", false),
                        gitUrl = if (obj.isNull("gitUrl")) null else obj.getString("gitUrl")
                    )
                )
            }
            list.sortedByDescending { it.lastOpened }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Saves the list of projects to global metadata.
     */
    private fun saveProjects(projects: List<Project>) {
        try {
            val array = JSONArray()
            for (proj in projects) {
                val obj = JSONObject().apply {
                    put("name", proj.name)
                    put("path", proj.path)
                    put("lastOpened", proj.lastOpened)
                    put("isArchived", proj.isArchived)
                    put("gitUrl", proj.gitUrl)
                }
                array.put(obj)
            }
            projectsFile.writeText(array.toString(4))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Create a new project workspace.
     */
    fun createProject(name: String, parentDirPath: String = defaultWorkspacesDir.absolutePath): Project {
        val safeName = name.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        var projectDir = File(parentDirPath, safeName)
        var count = 1
        while (projectDir.exists()) {
            projectDir = File(parentDirPath, "${safeName}_$count")
            count++
        }

        if (!projectDir.exists()) {
            if (!projectDir.mkdirs()) {
                throw IOException("Failed to create project directory: ${projectDir.absolutePath}")
            }
        }

        // Create hidden .delta directory
        val deltaDir = File(projectDir, ".delta")
        if (!deltaDir.exists()) {
            deltaDir.mkdirs()
        }

        // Initialize local .delta/config.json
        val configFile = File(deltaDir, "config.json")
        val configJson = JSONObject().apply {
            put("name", name)
            put("createdAt", System.currentTimeMillis())
            put("settings", JSONObject())
        }
        configFile.writeText(configJson.toString(4))

        val newProject = Project(
            name = name,
            path = projectDir.absolutePath,
            lastOpened = System.currentTimeMillis(),
            isArchived = false
        )

        val projects = getProjects().toMutableList()
        projects.add(newProject)
        saveProjects(projects)

        return newProject
    }

    /**
     * Imports an existing folder as a workspace.
     */
    fun importProject(path: String): Project {
        val folder = File(path)
        if (!folder.exists() || !folder.isDirectory) {
            throw IOException("Path does not exist or is not a directory: $path")
        }

        val name = folder.name
        val deltaDir = File(folder, ".delta")
        if (!deltaDir.exists()) {
            deltaDir.mkdirs()
        }

        val configFile = File(deltaDir, "config.json")
        if (!configFile.exists()) {
            val configJson = JSONObject().apply {
                put("name", name)
                put("createdAt", System.currentTimeMillis())
                put("settings", JSONObject())
            }
            configFile.writeText(configJson.toString(4))
        }

        val existingProjects = getProjects()
        val alreadyRegistered = existingProjects.firstOrNull { it.path == folder.absolutePath }
        if (alreadyRegistered != null) {
            // Update lastOpened
            val updated = alreadyRegistered.copy(lastOpened = System.currentTimeMillis())
            val newList = existingProjects.filter { it.path != folder.absolutePath }.toMutableList()
            newList.add(updated)
            saveProjects(newList)
            return updated
        }

        val newProj = Project(
            name = name,
            path = folder.absolutePath,
            lastOpened = System.currentTimeMillis(),
            isArchived = false
        )
        val newList = existingProjects.toMutableList()
        newList.add(newProj)
        saveProjects(newList)
        return newProj
    }

    /**
     * Renames a project workspace.
     */
    fun renameProject(project: Project, newName: String) {
        val currentDir = File(project.path)
        if (!currentDir.exists()) {
            throw IOException("Project directory does not exist: ${project.path}")
        }

        val parentDir = currentDir.parentFile ?: throw IOException("No parent directory found.")
        val safeName = newName.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        var newDir = File(parentDir, safeName)
        var count = 1
        while (newDir.exists() && newDir.absolutePath != currentDir.absolutePath) {
            newDir = File(parentDir, "${safeName}_$count")
            count++
        }

        if (newDir.absolutePath != currentDir.absolutePath) {
            if (!currentDir.renameTo(newDir)) {
                throw IOException("Failed to rename directory from ${currentDir.name} to ${newDir.name}")
            }
        }

        // Update config.json
        val deltaDir = File(newDir, ".delta")
        if (!deltaDir.exists()) deltaDir.mkdirs()
        val configFile = File(deltaDir, "config.json")
        val configJson = if (configFile.exists()) {
            JSONObject(configFile.readText())
        } else {
            JSONObject()
        }
        configJson.put("name", newName)
        configFile.writeText(configJson.toString(4))

        val projects = getProjects().map {
            if (it.path == project.path) {
                it.copy(name = newName, path = newDir.absolutePath, lastOpened = System.currentTimeMillis())
            } else {
                it
            }
        }
        saveProjects(projects)
    }

    /**
     * Duplicates a project workspace.
     */
    fun duplicateProject(project: Project): Project {
        val sourceDir = File(project.path)
        if (!sourceDir.exists()) {
            throw IOException("Source directory does not exist: ${project.path}")
        }

        val parentDir = sourceDir.parentFile ?: throw IOException("No parent directory found.")
        val newName = "${project.name}_Copy"
        var destDir = File(parentDir, sourceDir.name + "_Copy")
        var count = 1
        while (destDir.exists()) {
            destDir = File(parentDir, "${sourceDir.name}_Copy_$count")
            count++
        }

        // Copy directory files recursively (except .delta directory)
        copyDirectory(sourceDir, destDir, exclude = setOf(".delta"))

        // Create new .delta config
        val deltaDir = File(destDir, ".delta").apply { mkdirs() }
        val configFile = File(deltaDir, "config.json")
        val configJson = JSONObject().apply {
            put("name", newName)
            put("createdAt", System.currentTimeMillis())
            put("settings", JSONObject())
        }
        configFile.writeText(configJson.toString(4))

        val duplicatedProj = Project(
            name = newName,
            path = destDir.absolutePath,
            lastOpened = System.currentTimeMillis(),
            isArchived = false,
            gitUrl = project.gitUrl
        )

        val projects = getProjects().toMutableList()
        projects.add(duplicatedProj)
        saveProjects(projects)

        return duplicatedProj
    }

    /**
     * Deletes a project workspace directory & config.
     */
    fun deleteProject(project: Project) {
        val dir = File(project.path)
        if (dir.exists()) {
            deleteDirectoryRecursive(dir)
        }

        val projects = getProjects().filter { it.path != project.path }
        saveProjects(projects)
    }

    /**
     * Archive project in tracking metadata.
     */
    fun archiveProject(project: Project, archive: Boolean) {
        val projects = getProjects().map {
            if (it.path == project.path) {
                it.copy(isArchived = archive)
            } else {
                it
            }
        }
        saveProjects(projects)
    }

    /**
     * Sets a workspace last opened timestamp to top the list.
     */
    fun markProjectOpened(project: Project) {
        val projects = getProjects().map {
            if (it.path == project.path) {
                it.copy(lastOpened = System.currentTimeMillis())
            } else {
                it
            }
        }
        saveProjects(projects)
    }

    private fun copyDirectory(sourceLocation: File, targetLocation: File, exclude: Set<String> = emptySet()) {
        if (exclude.contains(sourceLocation.name)) return

        if (sourceLocation.isDirectory) {
            if (!targetLocation.exists()) {
                targetLocation.mkdirs()
            }
            val children = sourceLocation.list() ?: return
            for (i in children.indices) {
                copyDirectory(
                    File(sourceLocation, children[i]),
                    File(targetLocation, children[i]),
                    exclude
                )
            }
        } else {
            sourceLocation.copyTo(targetLocation, overwrite = true)
        }
    }

    private fun deleteDirectoryRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            val children = fileOrDirectory.listFiles()
            if (children != null) {
                for (child in children) {
                    deleteDirectoryRecursive(child)
                }
            }
        }
        fileOrDirectory.delete()
    }

    // AI Providers Management

    fun getAIProviders(): List<AIProvider> {
        if (!aiProvidersFile.exists()) {
            // Seed default providers
            val defaults = listOf(
                AIProvider("gemini", "Google Gemini", "", "gemini-1.5-pro", true),
                AIProvider("openai", "OpenAI GPT-4", "", "gpt-4o", false),
                AIProvider("anthropic", "Anthropic Claude", "", "claude-3-5-sonnet", false)
            )
            saveAIProviders(defaults)
            return defaults
        }
        return try {
            val content = aiProvidersFile.readText()
            val array = JSONArray(content)
            val list = mutableListOf<AIProvider>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    AIProvider(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        apiKey = obj.getString("apiKey"),
                        defaultModel = obj.getString("defaultModel"),
                        isEnabled = obj.getBoolean("isEnabled")
                    )
                )
            }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveAIProviders(providers: List<AIProvider>) {
        try {
            val array = JSONArray()
            for (provider in providers) {
                val obj = JSONObject().apply {
                    put("id", provider.id)
                    put("name", provider.name)
                    put("apiKey", provider.apiKey)
                    put("defaultModel", provider.defaultModel)
                    put("isEnabled", provider.isEnabled)
                }
                array.put(obj)
            }
            aiProvidersFile.writeText(array.toString(4))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateAIProvider(updated: AIProvider) {
        val providers = getAIProviders().map {
            if (it.id == updated.id) updated else it
        }
        saveAIProviders(providers)
    }
}
