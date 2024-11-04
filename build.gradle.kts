plugins {
    id("java")
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.github.dk"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.aliyun.com/repository/public") }
}

val jLineVersion = "3.27.1"
val gitlab4jVersion = "6.0.0-rc.6"

dependencies {
    implementation("org.jline:jline:${jLineVersion}")
    implementation("org.gitlab4j:gitlab4j-api:${gitlab4jVersion}")
//    implementation("javax.ws.rs:javax.ws.rs-api:2.1.1")
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}