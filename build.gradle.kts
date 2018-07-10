import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.dani09"
val artifactID = "http"
version = "0.1.0"

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.2.51"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

plugins {
    java
}

apply {
    plugin("kotlin")
}

val kotlinVersion: String by extra

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlinVersion))
    testCompile("junit", "junit", "4.12")

    implementation("commons-io:commons-io:2.6")
    implementation("org.json:json:20180130")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
