plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":application"))
    implementation(project(":core"))
    implementation(project(":infrastructure"))
    implementation(project(":docker-plugin"))
    implementation(project(":workflow-engine"))
    implementation(project(":observability"))
    implementation(project(":git-plugin"))
    implementation(project(":build-plugin"))
    implementation(project(":event-streaming"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("net.logstash.logback:logstash-logback-encoder:8.0")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("devops-toolbox.jar")
}
