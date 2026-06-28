dependencies {
    implementation(project(":core"))
    implementation(project(":application"))
    
    implementation("com.github.docker-java:docker-java:3.3.4")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter")
}
