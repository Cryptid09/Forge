dependencies {
    implementation(project(":core"))
    implementation(project(":application"))
    implementation(project(":workflow-engine"))
    implementation(project(":observability"))
    implementation(project(":git-plugin"))
    implementation(project(":build-plugin"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
}
