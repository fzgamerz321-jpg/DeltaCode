# Architecture Context

## Stack

| Layer | Technology | Role |
|--------|------------|------|
| Framework | Android + Kotlin | Native Android application |
| UI | Jetpack Compose + Material 3 (customized) | IDE interface and rendering |
| Editor Engine | Rust | High-performance text buffer, cursor engine, undo/redo, search, indexing |
| Native Bridge | JNI | Communication between Kotlin and Rust |
| Architecture | Clean Architecture + MVI | Modular application architecture |
| Dependency Injection | Hilt | Dependency management |
| Build System | Gradle Kotlin DSL | Build configuration |
| Navigation | Navigation Compose | Screen navigation |
| Storage | Android File System + DataStore | Application settings and workspace management |
| Workspace Metadata | JSON (.delta folder) | Project-specific settings and history |
| AI | Provider Abstraction Layer | Multiple AI providers through configurable APIs |
| Language Intelligence | Language Server Protocol (LSP) | Code completion, diagnostics, hover, formatting |
| Version Control | Git CLI via Terminal | Source control integration |
| Terminal | Embedded Terminal | Command execution and development workflows |
| Plugin System | VS Code Extension Compatibility Layer | Extension support |
| Search Engine | Rust Indexer + Ripgrep-style search | Fast project indexing and search |

---

## System Boundaries

- `app/` — Android launcher, dependency injection, startup, navigation, permissions.
- `core/` — Rust editor engine responsible for text manipulation, cursor logic, undo/redo, indexing, and JNI bridge.
- `editor/` — Compose-based editor viewport, tabs, minimap, selections, and rendering.
- `keyboard/` — Custom split programming keyboard, modifier engine, shortcut system.
- `gestures/` — Touch gesture recognition and routing for one-, two-, and three-finger interactions.
- `workspace/` — Workspace Dashboard, project manager, recent projects, templates, import/export, project metadata.
- `terminal/` — Embedded terminal sessions and shell integration.
- `git/` — Git UI that executes Git commands through the terminal.
- `lsp/` — Language Server Protocol adapters and language intelligence.
- `ai/` — AI provider abstraction, chat, inline completion, configurable model management.
- `plugins/` — Plugin runtime, extension compatibility layer, sandbox.
- `common/` — Shared utilities, models, helpers, constants.
- `buildSrc/` — Shared Gradle build logic.

---

## Storage Model

**Android Internal Storage**

- Global application settings
- User account information
- Installed plugins
- Installed themes
- AI provider configurations
- Downloaded language servers
- Cached resources

**Project Workspace (.delta folder)**

Each project contains its own hidden `.delta` folder storing:

- Workspace configuration
- Open tabs
- Cursor positions
- Editor history
- Terminal session state
- AI conversation history
- Project settings
- Installed project plugins
- Breakpoints
- Bookmarks
- Workspace cache

---

## Auth and Access Model

- Every user must sign in before using DeltaCode.
- Authentication supports multiple providers (Google, GitHub, Microsoft, Email).
- Each workspace belongs to a single authenticated user.
- Future collaboration features will use ownership and permission-based access control.
- AI providers authenticate independently using user-supplied API keys stored securely on the device.

---

## Invariants

1. Rust owns all editor logic including the text buffer, cursor movement, undo/redo, indexing, and search.
2. Jetpack Compose is responsible only for rendering the UI and forwarding user interactions.
3. UI components must never directly modify editor state; all state changes pass through the architecture layers.
4. Every major subsystem (editor, gestures, keyboard, AI, plugins, workspace, terminal, Git) remains isolated and communicates only through defined interfaces.
5. Projects remain fully portable because all workspace-specific metadata is stored inside the project's `.delta` directory.
6. The application must remain fully functional offline except for cloud AI providers and optional authentication services.
7. Git operations are executed through the embedded terminal using Git commands rather than custom Git implementations.
8. AI providers are fully interchangeable and can be added, removed, or switched without modifying editor logic.
9. Plugin execution must never compromise editor stability or application security.
10. The Workspace Dashboard is the primary entry point into the IDE; projects are created and managed there before opening in the editor.
11. The editor must support projects containing very large codebases while maintaining responsive interaction.
12. Every architectural decision must preserve modularity so future desktop synchronization, cloud services, and collaboration features can be added without major refactoring.