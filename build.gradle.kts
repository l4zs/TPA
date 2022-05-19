import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("io.papermc.paperweight.userdev") version "1.3.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "de.l4zs"
version = "1.1.1"

repositories {
    mavenCentral()
}

dependencies {
    // PaperMC Dependency
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    // KSpigot dependency
    implementation("net.axay", "kspigot", "1.18.2")
}

tasks {
    build {
        dependsOn(reobfJar)
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

bukkit {
    name = "TPA"
    apiVersion = "1.18"
    authors = listOf(
        "l4zs",
    )
    main = "$group.tpa.TPA"
    version = getVersion().toString()
    libraries = listOf(
        "net.axay:kspigot:1.18.2",
    )
}
