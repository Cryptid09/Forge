# Forge Roadmap

Forge is being developed in iterative phases. This document tracks what has been accomplished and our vision for the future.

## Completed Phases

- **Phase 0: Foundation**
  - Modular monolith skeleton
  - Spring Boot API & Frontend React dashboard setup
  - Local PostgreSQL persistence

- **Phase 1-3: Runtime & Tooling**
  - `WorkflowEngine` abstraction
  - Tool capability interface
  - Base job and event definitions

- **Phase 4: Docker Plugin**
  - Native Docker socket integration
  - Container and Image lifecycle management
  - Live log streaming via WebSockets

- **Phase 5: Workflow Engine Expansion**
  - Step implementations (Script, HTTP, Docker)
  - Workflow execution persistence

- **Phase 6: Observability**
  - Log Analyzer and terminal integration

- **Phase 7: Git & Build Integration**
  - `git-plugin` and `build-plugin` for Maven/Gradle capability detection

- **Phase 8: Automation Engine (Current)**
  - Workspace management
  - Local repository discovery
  - Cron scheduling and Automation Trigger abstractions

## Future Phases

- **Phase 9: Kubernetes Plugin**
  - K8s manifest application
  - Pod log streaming and resource visualization

- **Phase 10: Plugin SDK**
  - Decoupled plugin loading mechanism for community extensions

- **Phase 11: Remote Agents**
  - Allow Forge to execute jobs on remote servers via SSH or a lightweight agent

- **Phase 12: Marketplace**
  - Public directory to share and download workflows and plugins
