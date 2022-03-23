import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.papermc.paperweight.userdev") version "1.3.5"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "de.l4zs"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // PaperMC Dependency
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    // KSpigot dependency
    implementation("net.axay", "kspigot", "1.18.2")

    // Kotlinx.Coroutines dependency
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.0")
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.6.0")
}

tasks {
    jar {
        // Disabled, because we use the shadowJar task for building our jar
        enabled = false
    }
    build {
        dependsOn(reobfJar, shadowJar)
    }
    shadowJar {
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

bukkit {
    name = "ATPA"
    apiVersion = "1.18"
    authors = listOf(
        "l4zs",
    )
    main = "$group.tpa.TPA"
    version = getVersion().toString()
//    libraries = listOf(
//        "net.axay:kspigot:1.18.0",
//        "org.jetbrains.kotlin:kotlin-stdlib:1.6.10",
//        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0",
//        "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.0",
//        "com.mojang:brigadier:1.0.18",
//        "com.google.code.gson:gson:2.8.9",
//    )
}
