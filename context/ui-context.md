# UI Context

## Theme

DeltaCode follows a professional desktop IDE design language inspired primarily by Visual Studio Code while being redesigned specifically for Android landscape devices. The application is **dark-mode only** to reduce eye strain during long coding sessions and to maximize syntax highlighting contrast.

The UI emphasizes productivity over decoration. Every interface element exists for a purpose and should feel familiar to developers transitioning from desktop IDEs.

Design principles:

- Desktop IDE familiarity
- Touch-first interaction
- Landscape-first layouts
- Minimal distractions
- High information density
- Smooth 60 FPS animations
- Consistent spacing
- Layered surfaces
- Clear visual hierarchy
- Accessibility first

Interactive elements should use subtle motion rather than flashy animations. Every transition should communicate state changes.

---

# Colors

Never hardcode colors.

Every component must reference theme tokens.

| Role | Variable | Value |
|--------|----------|------------|
| Background | `--bg-base` | `#1E1E1E` |
| Elevated Background | `--bg-elevated` | `#252526` |
| Surface | `--bg-surface` | `#2D2D30` |
| Surface Hover | `--bg-hover` | `#37373D` |
| Surface Active | `--bg-active` | `#3E3E42` |
| Border | `--border-default` | `#3C3C3C` |
| Divider | `--border-light` | `#444444` |
| Primary Text | `--text-primary` | `#CCCCCC` |
| Secondary Text | `--text-secondary` | `#9D9D9D` |
| Muted Text | `--text-muted` | `#808080` |
| Disabled | `--text-disabled` | `#5A5A5A` |
| Primary Accent | `--accent-primary` | `#007ACC` |
| Accent Hover | `--accent-hover` | `#1F8FFF` |
| Selection | `--selection` | `#264F78` |
| Cursor | `--cursor` | `#AEAFAD` |
| Error | `--state-error` | `#F14C4C` |
| Warning | `--state-warning` | `#CCA700` |
| Success | `--state-success` | `#4EC9B0` |
| Info | `--state-info` | `#3794FF` |

---

# Typography

| Role | Font | Variable |
|---------|-------------|----------------|
| UI | Inter | `--font-ui` |
| Code | JetBrains Mono | `--font-code` |

Typography rules:

UI Text

- 14sp default
- Medium weight
- Consistent spacing
- High readability

Editor

- JetBrains Mono
- Ligatures optional
- Adjustable font size
- Adjustable line height

---

# Border Radius

DeltaCode intentionally uses very subtle corner rounding similar to VS Code.

| Context | Radius |
|-----------|-----------|
| Buttons | 6dp |
| Cards | 10dp |
| Panels | 8dp |
| Floating Toolbar | 12dp |
| Dialog | 14dp |
| Context Menu | 10dp |
| AI Panel | 12dp |
| Toast | 999dp |

Avoid excessive rounding.

---

# Component Library

Framework:

- Jetpack Compose Material 3

Component hierarchy:

- Reusable composables only
- Stateless UI whenever possible
- State hoisted to ViewModels
- Shared design tokens
- Shared spacing system
- Shared typography
- Shared elevation
- Shared animations

Never duplicate UI components.

---

# Layout Patterns

## Home Dashboard

- Full-screen workspace manager
- Recent projects grid
- Quick actions
- Templates
- AI Providers
- Extensions
- Search bar
- Settings shortcut

---

## IDE Layout

Landscape only.

```
┌───────────────────────────────────────────────────────────────┐
│ Activity Bar │ Workspace │ Editor │ AI Panel │
├───────────────────────────────────────────────────────────────┤
│ Split Programming Keyboard │
├───────────────────────────────────────────────────────────────┤
│ Status Bar │
└───────────────────────────────────────────────────────────────┘
```

Panels should be independently collapsible.

---

## Activity Bar

- VS Code inspired
- Left side
- Icon only
- Collapsible
- Animated indicator
- Active state highlight

---

## Workspace Explorer

Resizable panel.

Contains:

- Files
- Folders
- Search
- Outline
- Git
- Debug

---

## Editor

Largest visible component.

Contains:

- Tabs
- Breadcrumb
- Gutter
- Line Numbers
- Folding
- Minimap
- Cursor
- Diagnostics

---

## Terminal

Bottom slide-up panel.

Supports:

- Multiple tabs
- Split terminals
- Search
- Selection
- Command history

---

## AI Chat

Slide-in right panel.

Contains:

- Chat history
- Streaming output
- Code actions
- Model selector
- Context indicator
- Attach file
- Clear chat

---

## Dialogs

Centered.

Background blur.

Escape gesture closes.

---

## Bottom Keyboard

Always visible while editing.

Split layout.

Never overlaps editor.

Automatically adapts to screen size.

---

# Icons

Icon Library:

**Material Symbols Rounded**

Reason:

- Native Android
- Continuously maintained
- Excellent touch readability
- Large icon library
- Matches Material 3

Rules:

- Navigation icons → 24dp
- Toolbar icons → 20dp
- Inline icons → 18dp
- Status icons → 16dp

Never mix icon packs.

---

# Animations

All animations should run at 60 FPS.

Duration:

- Hover → 100ms
- Press → 80ms
- Expand → 220ms
- Collapse → 220ms
- Panel Slide → 250ms
- Dialog → 200ms
- Workspace Transition → 250ms

Use spring animations only for gesture-driven interactions.

---

# Responsive Rules

Supported devices:

- Phones
- Foldables
- Tablets

Portrait mode is **not supported**.

Landscape is mandatory.

UI should scale using adaptive panes rather than stretching.

---

# Touch Design Principles

Everything must be reachable without precision tapping.

Minimum touch target:

48dp × 48dp

Every gesture should provide:

- Haptic feedback
- Visual feedback
- Smooth animation

No gesture should conflict with another gesture.

---

# Beginner Mode

On first launch:

- Highlight important UI.
- Explain every panel.
- Demonstrate gestures.
- Explain keyboard shortcuts.
- Explain Activity Bar.
- Explain Workspace.
- Explain AI.
- Explain Terminal.
- Explain Git.
- Explain Extensions.

Users can replay the tutorial anytime from Settings.

---

# Visual Consistency Rules

Every screen in DeltaCode must feel like part of the same application.

Never introduce a new design language for individual features.

Follow these priorities:

1. Consistency
2. Readability
3. Performance
4. Accessibility
5. Familiarity with VS Code
6. Touch optimization

If a UI decision conflicts with desktop conventions, prefer the solution that improves touch usability without sacrificing productivity.