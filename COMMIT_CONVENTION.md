# PiscesCup Develop Lib – Commit Message Convention

This document defines the official commit message convention for **PiscesCup Develop Lib**,
a long-term maintained Minecraft / Fabric development library.

The goal is to keep commit history:
- clear and readable
- easy to review and revert
- friendly to changelog & release automation

This convention is based on **Conventional Commits**, with project-specific adaptations.

---

## 1. Commit Message Format

```
<type>(<scope>): <subject>
```

Optional extended format:

```
<type>(<scope>)!: <subject>

<body>

<footer>
```

### Fields
- **type**: category of the change (required)
- **scope**: affected module or area (recommended)
- **subject**: short summary of the change (required)
- **!**: marks a breaking change
- **body**: detailed explanation (optional)
- **footer**: breaking change details, issue references, etc. (optional)

---

## 2. Commit Types

| Type | Description |
| ---- | ----------- |
| feat | New feature or capability |
| fix | Bug fix |
| refactor | Code refactoring |
| perf | Performance improvement |
| docs | Documentation only changes |
| test | Test related changes |
| build | Build system or dependency changes |
| ci | CI/CD related changes |
| chore | Miscellaneous maintenance tasks |
| revert | Revert a previous commit |

---

## 3. Scope Convention

Common scopes used in this project:

- register
- item / block / entity
- item-group
- tag
- datagen
- lang
- mixin
- util
- api
- internal
- gradle
- deps
- release

---

## 4. Subject Rules

- Use present tense verbs (add, fix, refactor, remove)
- Keep under 72 characters
- No trailing period

---

## 5. Breaking Changes

Mark breaking changes with `!` and explain in footer.

Example:

```
refactor(register)!: redesign builder stages

BREAKING CHANGE: API signature changed.
```

---

## 6. Commit Granularity

- One logical change per commit
- Avoid mixing unrelated changes

---

## 7. Common Templates
New Feature
feat(item-group): add context-aware entry collector

Bug Fix
```
fix(register): ensure registryKey is initialized before get()
```

Refactor
```
refactor(util): extract NullCheck into CheckUtil
```

Performance
```
perf(lang): avoid repeated map lookups in Translation#putTranslation
```

Build System
```
build(gradle): align Java toolchain with language level 25
```

Breaking Change
```
refactor(register)!: simplify Pre/Post generics with CRTP

BREAKING CHANGE: PostRegistrable signature changed.
Migration: update extends clauses accordingly.
```

---
Happy coding 🚀  
— PiscesCup Develop Lib
