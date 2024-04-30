import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("io.papermc.paperweight.userdev") version "1.3.11"
    id("xyz.jpenilla.run-paper") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("plugin.serialization") version "1.8.21"
}

group = "cz.lukynka"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.incendo:cloud-core:2.0.0-beta.2")
    implementation("org.incendo:cloud-kotlin-extensions:2.0.0-beta.2")
    implementation("org.incendo:cloud-paper:2.0.0-beta.2")
    implementation("org.incendo:cloud-minecraft-extras:2.0.0-beta.2")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.9"
}