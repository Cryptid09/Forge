pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/release") }
    }
}

rootProject.name = "devops-toolbox"

include("bootstrap", "api", "application", "core", "infrastructure", "docker-plugin", "workflow-engine", "observability", "git-plugin", "build-plugin", "event-streaming")

project(":bootstrap").projectDir = file("bootstrap")
project(":api").projectDir = file("api")
project(":application").projectDir = file("application")
project(":core").projectDir = file("core")
project(":infrastructure").projectDir = file("infrastructure")
project(":docker-plugin").projectDir = file("docker-plugin")
project(":workflow-engine").projectDir = file("workflow-engine")
project(":observability").projectDir = file("observability")
project(":git-plugin").projectDir = file("git-plugin")
project(":build-plugin").projectDir = file("build-plugin")
project(":event-streaming").projectDir = file("event-streaming")
