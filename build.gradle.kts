plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.2.0-RC2"
    kotlin("plugin.serialization") version "2.2.0-RC2"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
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
    implementation(kotlin("stdlib"))
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}


tasks {
    runServer {
        minecraftVersion("1.21.1")
    }

    // jar = shadowJar
    build {
        dependsOn("shadowJar")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

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
