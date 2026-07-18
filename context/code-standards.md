# Code Standards

# Engineering Guidelines

## General

- Architecture must be modular and follow clean architecture principles.
- Every module must have a single responsibility and communicate through interfaces.
- Never implement temporary fixes, hacks, or duplicate logic—always fix the root cause.
- UI must never directly access business logic or native Rust code.
- Separate UI, editor engine, gesture engine, keyboard engine, AI engine, workspace, and plugin system.
- Every feature must be production-ready before moving to the next phase.
- Do not generate placeholder implementations unless explicitly requested.
- Every screen must work independently before integration.
- All animations must run at 60 FPS or better.
- Memory usage should remain optimized for devices with 4 GB RAM or lower.
- Keep code readable, documented, and scalable.
- Design everything with future desktop synchronization in mind.
- Landscape mode is mandatory; portrait mode is not supported.
- Avoid unnecessary recompositions and object allocations.
- Every public function and class should have documentation comments.

---

# Kotlin

- Kotlin is the primary language for Android UI.
- Enable strict null safety.
- Avoid lateinit unless absolutely necessary.
- Prefer immutable state.
- Use data classes wherever appropriate.
- Follow MVVM architecture.
- Never place business logic inside Composable functions.
- Keep ViewModels UI-independent.
- Use sealed classes for UI state and editor events.
- Use Kotlin Coroutines and Flow for asynchronous operations.
- Avoid global mutable state.
- Every module should expose only necessary APIs.

---

# Jetpack Compose

- Compose is the only UI framework.
- Material3 is used only as a base—not as the design language.
- Build custom IDE components instead of standard Android widgets.
- Keep composables small and reusable.
- UI should react entirely through state.
- Avoid unnecessary recompositions.
- Use remember only where appropriate.
- State should be hoisted whenever possible.
- No XML layouts.
- Support only landscape orientation.
- Maintain smooth gesture interaction at all times.

---

# Rust Core

- Rust is responsible for editor performance.
- Text buffer must be written in Rust.
- Cursor movement must be Rust-driven.
- Multi-cursor support belongs in Rust.
- Undo/Redo engine belongs in Rust.
- Search indexing belongs in Rust.
- Syntax parsing belongs in Rust where possible.
- JNI interface should remain thin.
- No UI logic inside Rust.
- Rust modules should be platform-independent.

---

# Gesture Engine

- Gesture recognition must be isolated into its own module.
- Never place gesture logic directly inside editor composables.
- One-finger gestures control cursor movement.
- Two-finger gestures control scrolling and floating toolbar.
- Three-finger gestures control quick actions and Activity Bar.
- Gesture recognition must work simultaneously with editor rendering.
- Touch latency should remain below 16 ms.
- Gestures should never conflict with typing.

---

# Keyboard Engine

- Keyboard is a standalone module.
- Keys are fully custom drawn.
- No Android system keyboard dependency.
- Support modifier key locking.
- Implement Shift, Ctrl, Alt, Delta, Caps Lock states.
- Long press activates lock mode.
- Support repeat keys.
- Support programmable shortcuts.
- Keyboard layout should be configurable internally.
- Future plugin support must be possible.

---

# AI System

- AI providers must be interchangeable.
- Never hardcode a specific AI model.
- AI communicates through an abstraction layer.
- Support offline models in future.
- AI must understand the workspace context.
- AI should never directly modify files without user approval.
- AI requests must be cancellable.
- Streaming responses are required.
- Conversation history belongs to workspace sessions.

---

# Plugin Framework

- Plugins are isolated modules.
- Plugins communicate only through exposed APIs.
- No plugin may directly access editor internals.
- Plugins must be sandboxed.
- Plugins can contribute:
  - Commands
  - Panels
  - Themes
  - Languages
  - AI Providers
  - Keyboard shortcuts
  - Tool windows

---

# LSP

- Use Microsoft's Language Server Protocol.
- Every language connects through LSP adapters.
- Editor never directly parses programming languages.
- Diagnostics should update incrementally.
- Completion should be asynchronous.
- Hover information must be cached.
- Semantic highlighting should be supported.

---

# Terminal

- Terminal is independent from the editor.
- Multiple terminal tabs.
- Background execution support.
- ANSI colors.
- Copy/Paste support.
- Session persistence.
- Future SSH support.

---

# Git

- Git module remains independent.
- Built-in diff viewer.
- Commit history.
- Branch switching.
- Merge conflict visualization.
- Integrated blame view.
- Future GitHub and GitLab support.

---

# Workspace

- Workspace controls projects.
- File explorer is independent.
- Multiple workspace support.
- Recent project history.
- File watcher.
- Large project optimization.
- Workspace state persistence.

---

# Styling

- Entire UI follows VS Code-inspired desktop proportions.
- No Android-looking components.
- Rounded corners only where appropriate.
- Use design tokens.
- No hardcoded colors.
- Support future themes.
- Smooth transitions.
- Consistent spacing system.
- Pixel-perfect alignment.

---

# Performance

- App startup under 2 seconds.
- Scroll at 120 FPS where hardware supports it.
- Cursor latency under 8 ms.
- Handle projects with over 100,000 files.
- Open files larger than 100 MB.
- Efficient memory usage.
- Lazy load all heavy components.
- Background indexing.

---

# File Organization

- `app/` — Android launcher, dependency injection, startup configuration.
- `core/` — Rust editor engine, JNI bridge, text buffer, parser.
- `editor/` — Editor rendering, viewport, cursor, selection, minimap, tabs.
- `keyboard/` — Split programming keyboard and shortcut engine.
- `gestures/` — Gesture recognition, touch routing, interaction system.
- `workspace/` — Projects, file explorer, workspace management.
- `terminal/` — Terminal emulator and shell integration.
- `git/` — Git operations and repository management.
- `lsp/` — Language Server Protocol integration.
- `ai/` — AI abstraction layer, providers, chat, inline completion.
- `plugins/` — Plugin SDK and runtime.
- `themes/` — Themes, icons, fonts, color schemes.
- `settings/` — User preferences and configuration.
- `common/` — Shared utilities, models, helpers, constants.
- `buildSrc/` — Shared Gradle configuration and build logic.
- `docs/` — Architecture documentation and developer guides.
- `tests/` — Unit, UI, integration, and performance tests.
