import java.net.URI

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "dev.elysium.eraces"

version = "1.9.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    // Reflections
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("io.github.classgraph:classgraph:4.8.184")

    // Paper
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    // Libraries
    compileOnly("org.xerial:sqlite-jdbc:3.51.0.0")
    compileOnly("org.luaj:luaj-jse:3.0.1")

    // Other plugin integrations
    compileOnly("me.clip:placeholderapi:2.11.6")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
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
        minecraftVersion("1.21.4")

        doFirst {
            val pluginsDir = file("run/plugins")
            pluginsDir.mkdirs()

            val viaVersionJar = pluginsDir.resolve("ViaVersion-5.5.1.jar")
            if (!viaVersionJar.exists()) {
                println("Скачиваем ViaVersion...")
                URI("https://hangarcdn.papermc.io/plugins/ViaVersion/ViaVersion/versions/5.6.0/PAPER/ViaVersion-5.6.0.jar").toURL()
                    .openStream().use { input ->
                    viaVersionJar.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                println("ViaVersion скачан в run/plugins")
            }
        }
    }

    // jar = shadowJar
    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveClassifier.set("") // чтобы jar назывался как обычно, без "-all"
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "eraces"
            version = project.version.toString()
        }
    }
}
