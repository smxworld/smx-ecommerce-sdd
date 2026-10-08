---
title: "Reorganise module internals into controller, service and repository packages"
status: applied
author: ""
created-at: "2026-09-12T00:00:00.000Z"
---

# Reorganise module internals into controller, service and repository packages

## Description

Each backend module currently divides its internals into
`application`, `domain` and `infrastructure`, a layout borrowed from
hexagonal architecture. Spring Modulith does not require it: Modulith
enforces one thing only, that everything below `internal/` is private
to the module, and is indifferent to how that space is organised.

The layout has not repaid its cost. Finding a query means checking
three packages across modules, and the separation it was meant to
guarantee has not held anyway. The structure was
elaborate enough to slow navigation and not observed strictly enough
to deliver the isolation it promised.

A structure nobody follows is worse than a simpler one everybody
follows. `controller` / `service` / `repository` says plainly what
lives where, matches the conventions.

## Scope

**This change moves code. It does not change behaviour.**

No method body is altered, no logic is extracted, no dependency is
added or removed. After this change the application must behave
exactly as before.

The extraction of queries out of services — the defect this layout
failed to prevent — is deliberately *not* part of this change. It is
addressed separately, once the structure is in place and the
architectural test can identify every offending class automatically.

Keeping the two apart matters: if a move and a restructuring happen
together and something breaks, there is no way to tell which caused
it.

## Changes

### New layout

```
com.bettingagent.{module}/
├── {Module}Api.java          ← unchanged, the module's only public face
└── internal/
    ├── controller/           ← REST controllers
    ├── service/              ← application services, orchestration
    ├── repository/           ← database access
    └── model/                ← entities, records, value objects
```

### Mapping from the current layout

| Current | New |
|---|---|
| `internal/infrastructure/*Controller.java` | `internal/controller/` |
| `internal/infrastructure/*Repository.java` | `internal/repository/` |
| `internal/infrastructure/` — other (filters, clients, config) | `internal/service/`, or `internal/repository/` if database-related |
| `internal/application/` | `internal/service/` |
| `internal/domain/` | `internal/model/` |

Classes whose placement is ambiguous — `ActiveUserFilter`,
`AclInterceptor`, `SecurityConfig`, `MlServiceClient` — go to
`internal/service/`. They are neither controllers nor repositories, and
inventing a fourth package for them would reintroduce the dispersion
this change removes.

### Package declarations and imports

Every moved class has its `package` declaration updated, and every
import referring to it corrected. Modern IDEs and refactoring tools do
this reliably; it should not be done by hand.

### `package-info.java`

The `@ApplicationModule` annotation stays where it is, at the module
root. It is unaffected.

## Verification

The change is correct when:

1. The project compiles
2. `mvn test` passes with no test modified
3. `ApplicationModules.of(BettingAgentApplication.class).verify()` still
   passes, confirming module boundaries are intact
4. `git diff --stat` shows only file moves and package/import lines —
   no change inside a method body

Point 4 is the important one. Any diff line that is not a package
declaration, an import, or a file rename indicates the change exceeded
its scope.

## Acceptance criteria

1. Every module's internals are organised as `controller`, `service`,
   `repository`, `model`
2. The `application`, `domain` and `infrastructure` packages no longer
   exist
3. `{Module}Api.java` remains the only public type of each module
4. No method body is modified
5. The test suite passes without any test being changed
6. Modulith's own verification passes
7. `system/architecture.md` is updated to describe the new layout and
   to record that the previous one was abandoned, so the reason is not
   lost
