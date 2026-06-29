# DevOps Toolbox Tutorial

Welcome to DevOps Toolbox! This tutorial will guide you through the core workflows of the platform, from getting started to executing your first operational tasks.

## Prerequisites

Before starting, ensure you have the following installed on your machine:
- **Docker** and **Docker Compose**: Used to run the complete local stack.

## 1. Starting the Platform Local Stack

DevOps Toolbox is designed to be easily run locally using Docker Compose. 

1. Open your terminal in the root directory of the project.
2. Run the following command to start all services:
   ```bash
   docker compose up --build
   ```
3. Wait for the services (frontend, backend, database, and any streaming services) to initialize. 
4. Once everything is running, open your web browser and navigate to the local dashboard (usually `http://localhost:3000`, check your compose configuration).

## 2. Navigating the Dashboard

When you open the application, you'll be presented with the main Control Plane UI. The sidebar navigation provides access to key product areas:

- **Dashboard**: A high-level overview of system status.
- **Tools**: The catalog of operational tools available for execution.
- **Jobs**: Historical and active execution records.
- **Events**: A real-time timeline of what is happening across the platform.
- **Workflows**: Multi-step operational processes.
- **Docker & Git**: Specific integrations for container and repository management.
- **Observability**: Terminal, logs, and metrics.

## 3. The Core Product Loop: Executing a Tool

The primary intended user experience in DevOps Toolbox is running tools and observing their outcomes. Let's walk through that core loop.

### Step 1: Pick a Tool
Navigate to the **Tools** section using the sidebar menu. Here you will see a list of available automation modules. Select a tool you wish to run (for example, a demo tool or a simple operational utility).

### Step 2: Execute It
Once you've selected a tool, fill out any required parameters in the execution form and click the button to trigger the tool. The backend will treat this as a formal execution contract.

### Step 3: Watch the Job Update
After execution begins, a **Job** is created. You will be able to see the job's status, duration, output, and whether it succeeds or fails. Because DevOps Toolbox combines persisted history with live updates (WebSockets), you don't need to manually refresh the page to see progress.

### Step 4: Follow the Event Stream
Navigate to the **Events** page. Every job run and system action generates events. This chronological record provides an audit trail and timeline of platform activity.

### Step 5: Inspect Telemetry
To dig deeper into the results of your execution, you can check the **Logs and log analysis** section or review the **Kafka, audit, metrics, and notifications** dashboards depending on what the tool affects.

## Next Steps

Now that you've completed a basic tool execution, you can explore more advanced features:
- **Workflows**: Try orchestrating multiple tools into a higher-level process in the Workflows section.
- **Container Operations**: Navigate to the Docker section to interact with your local Docker resources directly from the control plane.
- **Extending the Platform**: Since DevOps Toolbox is extensible by design, you can review the backend tool descriptors to understand how new automation modules can be plugged into the platform.

Enjoy your centralized operational control plane!
