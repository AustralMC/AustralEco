import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.australmc"
version = "1.0.0"
description = "AustralEco"
java.sourceCompatibility = JavaVersion.VERSION_17

object Versions {
    const val CORE = "1.0.0"
}

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenLocal()

    mavenCentral()

    maven { url = uri("https://jitpack.io/") }

    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("net.australmc:AustralCore:${Versions.CORE}")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["kotlin"])
    }
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks

compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}