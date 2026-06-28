dependencies {
    implementation(project(":application"))
    implementation(project(":core"))
    implementation(project(":workflow-engine"))
    implementation(project(":infrastructure"))
    implementation(project(":observability"))
    implementation(project(":git-plugin"))
    implementation(project(":build-plugin"))
    implementation(project(":event-streaming"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}
