plugins {
    kotlin("jvm") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("io.papermc.paperweight.userdev") version "1.3.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "de.l4zs"
version = "1.1.2"

repositories {
    mavenCentral()
}

dependencies {
    // PaperMC Dependency
    paperDevBundle("1.19-R0.1-SNAPSHOT")

    // KSpigot dependency
    implementation("net.axay", "kspigot", "1.19.0")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjdk-release=17"
            )
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
        }
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
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
        "net.axay:kspigot:1.19.0",
    )
}
