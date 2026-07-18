package com.deltacode.deltacode

import com.deltacode.deltacode.data.storage.WorkspaceManager
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class WorkspaceManagerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var workspaceDir: File
    private lateinit var workspaceManager: WorkspaceManager

    @Before
    fun setUp() {
        workspaceDir = tempFolder.newFolder("test_workspace")
        workspaceManager = WorkspaceManager(workspaceDir)
    }

    @After
    fun tearDown() {
        // TemporaryFolder rules auto delete files
    }

    @Test
    fun testCreateProject() {
        val newProj = workspaceManager.createProject("My New Project")
        assertEquals("My New Project", newProj.name)
        val projectDir = File(newProj.path)
        assertTrue(projectDir.exists())
        assertTrue(projectDir.isDirectory)

        // Check if .delta and config.json are created
        val deltaDir = File(projectDir, ".delta")
        assertTrue(deltaDir.exists())
        assertTrue(File(deltaDir, "config.json").exists())

        // Check if registered in project list
        val projects = workspaceManager.getProjects()
        assertEquals(1, projects.size)
        assertEquals("My New Project", projects[0].name)
    }

    @Test
    fun testImportProject() {
        // Create an un-tracked folder
        val untrackedFolder = tempFolder.newFolder("untracked_project")
        val testFile = File(untrackedFolder, "main.kt")
        testFile.writeText("println(\"Hello\")")

        // Import project
        val project = workspaceManager.importProject(untrackedFolder.absolutePath)
        assertEquals("untracked_project", project.name)

        val deltaDir = File(untrackedFolder, ".delta")
        assertTrue(deltaDir.exists())
        assertTrue(File(deltaDir, "config.json").exists())

        val projects = workspaceManager.getProjects()
        assertEquals(1, projects.size)
        assertEquals("untracked_project", projects[0].name)
    }

    @Test
    fun testRenameProject() {
        val project = workspaceManager.createProject("Old Name")
        workspaceManager.renameProject(project, "New Awesome Name")

        val projects = workspaceManager.getProjects()
        assertEquals(1, projects.size)
        assertEquals("New Awesome Name", projects[0].name)

        // Verify folder rename and config file logic
        val newDir = File(projects[0].path)
        assertTrue(newDir.exists())
        assertTrue(newDir.name.startsWith("New_Awesome_Name"))
        assertTrue(File(newDir, ".delta/config.json").exists())
    }

    @Test
    fun testDuplicateProject() {
        val project = workspaceManager.createProject("Source Proj")
        val sourceDir = File(project.path)
        // Add a stub file to copy
        val codeFile = File(sourceDir, "Activity.kt")
        codeFile.writeText("class MainActivity {}")

        val duplicated = workspaceManager.duplicateProject(project)
        assertEquals("Source Proj_Copy", duplicated.name)

        // Verify folder created and files copied
        val dupDir = File(duplicated.path)
        assertTrue(dupDir.exists())
        val copiedFile = File(dupDir, "Activity.kt")
        assertTrue(copiedFile.exists())
        assertEquals("class MainActivity {}", copiedFile.readText())

        // Verify is indexed in project metadata
        val projects = workspaceManager.getProjects()
        assertEquals(2, projects.size)
        assertTrue(projects.any { it.name == "Source Proj" })
        assertTrue(projects.any { it.name == "Source Proj_Copy" })
    }

    @Test
    fun testDeleteProject() {
        val project = workspaceManager.createProject("Delete Me")
        val projectDir = File(project.path)
        assertTrue(projectDir.exists())

        workspaceManager.deleteProject(project)
        assertFalse(projectDir.exists())

        val projects = workspaceManager.getProjects()
        assertTrue(projects.isEmpty())
    }

    @Test
    fun testArchiveProject() {
        val project = workspaceManager.createProject("Archive Me")
        assertFalse(workspaceManager.getProjects()[0].isArchived)

        workspaceManager.archiveProject(project, true)
        assertTrue(workspaceManager.getProjects()[0].isArchived)

        workspaceManager.archiveProject(project, false)
        assertFalse(workspaceManager.getProjects()[0].isArchived)
    }

    @Test
    fun testAIProvidersSeedingAndCRUD() {
        val providers = workspaceManager.getAIProviders()
        assertEquals(3, providers.size) // defaults seeded
        assertTrue(providers.any { it.id == "gemini" })

        // Update default model
        val originalGemini = providers.first { it.id == "gemini" }
        val updatedGemini = originalGemini.copy(defaultModel = "gemini-2.0-flash")
        workspaceManager.updateAIProvider(updatedGemini)

        val retrieved = workspaceManager.getAIProviders().first { it.id == "gemini" }
        assertEquals("gemini-2.0-flash", retrieved.defaultModel)
    }
}
