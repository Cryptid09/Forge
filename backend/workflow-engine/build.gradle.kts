dependencies {
    implementation(project(":core"))
    implementation(project(":application"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.core:jackson-databind")
}
