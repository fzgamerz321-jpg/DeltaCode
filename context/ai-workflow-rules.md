# AI Workflow Rules

## Approach

DeltaCode is developed using a specification-driven, architecture-first workflow. Every implementation must follow the documentation inside the `/docs` directory, including `project-overview.md`, `architecture.md`, `ui-context.md`, `gesture-spec.md`, `keyboard-spec.md`, `progress-tracker.md`, and any future design documents.

The documentation is the single source of truth. Never invent behavior, UI, architecture, workflows, or APIs that are not explicitly defined. When documentation and implementation differ, the documentation always takes precedence until intentionally updated.

Implement features incrementally in small, testable units that compile successfully before moving to the next feature.

---

# Scoping Rules

- Work on exactly one feature module at a time.
- Finish one feature completely before starting another.
- Never mix unrelated modules in the same implementation.
- Prefer many small commits over one massive change.
- Every completed feature must compile successfully.
- Every feature must be independently testable.
- Never leave partially implemented systems.
- Do not create placeholder implementations unless explicitly requested.
- Do not remove existing working functionality while implementing a new feature.
- Preserve backward compatibility between modules whenever possible.

---

# Feature Implementation Order

Always implement features in this order unless explicitly instructed otherwise:

1. Core architecture
2. Shared models
3. Storage layer
4. Workspace manager
5. Dashboard
6. Editor engine integration
7. Gesture engine
8. Split keyboard
9. Activity Bar
10. File Explorer
11. Editor Viewport
12. Terminal
13. Git
14. AI System
15. LSP
16. Plugin System
17. Debugger
18. Settings
19. Onboarding
20. Performance optimization

Do not skip ahead.

---

# When to Split Work

Split implementation immediately if it combines:

- UI implementation and backend/storage logic
- Multiple independent screens
- Multiple unrelated Compose components
- Rust core and Android UI
- Git and Terminal functionality
- AI and Workspace functionality
- Multiple storage models
- Multiple gesture systems
- Multiple keyboard systems
- More than one Gradle module
- More than one package boundary
- More than one major subsystem

If a feature cannot be completed, tested, and verified within one implementation cycle, it must be divided into smaller feature units.

---

# Handling Missing Requirements

Never guess.

If requirements are missing:

- Stop implementation.
- Search existing documentation.
- If documentation does not define the behavior, add the missing requirement to `progress-tracker.md` under **Open Questions**.
- Wait for clarification before implementing.

Never invent:

- UI layouts
- Gestures
- Keyboard shortcuts
- AI behavior
- Storage structure
- APIs
- Project workflow
- Navigation
- Settings
- Plugin behavior

---

# Code Quality Rules

Every implementation must:

- Compile successfully.
- Remove warnings when practical.
- Avoid duplicated code.
- Follow SOLID principles.
- Keep classes focused on one responsibility.
- Prefer composition over inheritance.
- Avoid global mutable state.
- Use dependency injection where appropriate.
- Avoid unnecessary abstractions.
- Write readable code before clever code.

---

# Protected Files

Do not modify unless explicitly instructed:

- Gradle Wrapper
- buildSrc/
- Rust Cargo configuration
- Third-party libraries
- Generated Compose resources
- Generated protobuf files
- Generated LSP bindings
- External SDK code
- VS Code compatibility layer
- JNI bridge unless working specifically on Rust integration

---

# Keeping Documentation in Sync

Whenever implementation changes architecture or behavior, update the relevant documentation immediately.

This includes:

- architecture.md
- project-overview.md
- ui-context.md
- gesture-spec.md
- keyboard-spec.md
- storage-model.md
- ai-system.md
- plugin-system.md
- progress-tracker.md

Documentation must never become outdated.

---

# Before Moving to the Next Feature

Verify all of the following:

- Feature compiles successfully.
- No Kotlin errors.
- No Rust errors.
- No broken imports.
- No unresolved references.
- Feature works end-to-end.
- No existing feature regressed.
- Documentation updated.
- progress-tracker.md updated.
- Architecture invariants preserved.
- Feature passes manual testing.

Only then continue.

---

# Error Handling

When encountering an error:

1. Identify the root cause.
2. Fix the root cause.
3. Never silence compiler errors.
4. Never comment out broken code.
5. Never replace functionality with placeholders.
6. Never delete working systems to satisfy compilation.
7. Explain the reason for architectural changes before making them.

---

# AI Agent Constraints

The AI agent must NEVER:

- Rewrite the entire project unnecessarily.
- Change folder structures without approval.
- Rename public APIs without documentation updates.
- Remove existing modules.
- Duplicate business logic.
- Create mock implementations without being asked.
- Ignore documentation.
- Skip compilation checks.
- Invent UX decisions.
- Invent gestures.
- Invent keyboard shortcuts.
- Invent AI capabilities.
- Invent storage formats.

The AI agent should always prefer extending existing systems rather than replacing them.

---

# Definition of Done

A feature is considered complete only when:

- It is fully implemented.
- It compiles successfully.
- It integrates with the existing architecture.
- It follows all documentation.
- It is manually testable.
- It does not introduce regressions.
- Documentation has been updated.
- progress-tracker.md reflects completion.
- The project still builds successfully.