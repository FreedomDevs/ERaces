plugins {
    java
    `maven-publish`
   // kotlin("jvm") version "1.9.10"
}

group = "dev.elysium.eraces"

version = "1.4.0"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    //implementation(kotlin("stdlib"))
    //testImplementation(kotlin("test"))
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)) }

tasks.processResources { filesMatching("plugin.yml") { expand(mapOf("version" to version)) } }

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "races-plugin"
            version = project.version.toString()
        }
    }
}
