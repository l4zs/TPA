val javaVersion = 17
val kspigotVersion = "1.19.0"

plugins {
    kotlin("jvm") version "1.7.20"
    id("io.papermc.paperweight.userdev") version "1.3.8"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.l4zs"
version = "1.1.2"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // PaperMC Dependency
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")

    // KSpigot dependency
    shadow("net.axay", "kspigot", kspigotVersion)

    shadow("de.l4zs", "translations", "1.0.0-SNAPSHOT-22-08-02-23-15")
}

tasks {
    jar {
        enabled = false
    }
    assemble {
        dependsOn(reobfJar, shadowJar)
    }
    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
    }
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjdk-release=$javaVersion",
                "-opt-in=kotlin.RequiresOptIn",
            )
            jvmTarget = "$javaVersion"
        }
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(javaVersion)
    }
}

bukkit {
    name = "TPA"
    apiVersion = "1.19"
    authors = listOf(
        "l4zs",
    )
    main = "$group.tpa.TPA"
    version = getVersion().toString()
    libraries = listOf(
//        "net.axay:kspigot:1.19.0",
    )
}
