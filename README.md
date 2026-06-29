<div align="center">
  <img src="frontend/public/Forge-logo.png" alt="Forge Logo" width="150"/>
  <h1>Forge</h1>
  <p>A Local-First, Self-Hosted DevOps Workstation</p>

  <p>
    <img src="https://img.shields.io/badge/Java-21+-blue.svg" alt="Java Version">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.2+-green.svg" alt="Spring Boot">
    <img src="https://img.shields.io/badge/React-18-blue.svg" alt="React">
    <img src="https://img.shields.io/badge/PostgreSQL-16-blue.svg" alt="PostgreSQL">
    <img src="https://img.shields.io/badge/Docker-Supported-blue.svg" alt="Docker">
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License"></a>
</div>

---

## 📖 Overview

**Forge** is a modern, modular monolith platform built for DevOps automation. It is designed to act as your ultimate local-first DevOps workstation, seamlessly orchestrating workflows, Docker containers, git operations, and continuous integration pipelines—all running entirely on your local machine or self-hosted server. 

Forge prioritizes a **local-first** approach: source code, SSH keys, Docker daemons, and build artifacts never leave your control unless explicitly configured.

---

## ✨ Key Features

- **Workspace Management:** Group your repositories intelligently into logical workspaces.
- **Automation & Workflows:** Define, schedule, and execute complex workflows via the built-in Automation Engine and Quartz Scheduler.
- **Docker Integration:** Direct integration with your local Docker daemon to build, run, and view logs of containers.
- **Git & Build Tooling:** Native detection and execution capabilities for Maven, Gradle, and Git.
- **Event-Driven Architecture:** Asynchronous event streaming powered by Apache Kafka for observability, metrics, and audit logging.
- **Modular Monolith Design:** Clean architecture adhering to Ports and Adapters, ensuring extreme decoupling between domain logic and infrastructure.

---

## 🏗️ Architecture Overview

The backend uses a strict layered architecture pattern to keep domain logic pure:
- **`bootstrap`**: Application entrypoint and dependency wiring.
- **`api`**: REST controllers and WebSocket handlers.
- **`application`**: Application services and use-case orchestration.
- **`core`**: Pure domain contracts and entities.
- **`infrastructure`**: Adapters for persistence (PostgreSQL), scheduling, and external APIs.
- **Plugins**: Pluggable modules for Docker, Git, Build, and Event Streaming.

*See the [ARCHITECTURE.md](docs/ARCHITECTURE.md) for detailed runtime flows and diagrams.*

---

## 🚀 Installation & Local Development

### Prerequisites
- Java 21+
- Node.js 22+
- Docker & Docker Compose

### Quick Start with Docker
The fastest way to get started is by spinning up the full platform using Docker Compose.

```bash
docker compose up -d
```
Navigate to [http://localhost:3000](http://localhost:3000) to view the Forge Dashboard.

### Local Development Setup

**Backend (Spring Boot)**
```bash
cd backend
./gradlew :bootstrap:bootRun
```
*Note: Ensure PostgreSQL and Kafka are running via docker-compose before starting the backend natively.*

**Frontend (React + Vite)**
```bash
cd frontend
npm install
npm run dev
```

---

## 🗺️ Roadmap
Forge is actively developed across distinct phases. From the foundational backend skeleton to advanced Kubernetes plugins, check out our progress in [ROADMAP.md](ROADMAP.md).

---

## 🤝 Contributing
We welcome community contributions! Please review our [Contributing Guidelines](CONTRIBUTING.md) to understand our coding standards, branch naming conventions, and Pull Request process.

---

## 🔒 Security
If you discover a security vulnerability, please refer to our [Security Policy](SECURITY.md) for responsible disclosure instructions.

---

## 📜 License
Forge is open-source software licensed under the [MIT License](LICENSE).
