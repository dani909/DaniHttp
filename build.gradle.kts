import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.dani09"
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
    `maven-publish`
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

    compile("commons-io:commons-io:2.6")
    compile("org.json:json:20180130")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(java.sourceSets["main"].allSource)
}

publishing {
    repositories {
        maven {
            url = uri("https://api.bintray.com/maven/dani09/DaniHttp/DaniHttp/;publish=1")
            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
    }
    (publications) {
        "mavenJava"(MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

tasks {
    val fullJar by creating(Jar::class) {
        baseName = "${project.name}-with-dependencies"
        from(configurations.runtime.map { if (it.isDirectory) it as Any else zipTree(it) })
        with(tasks["jar"] as CopySpec)
    }

    "assemble"{
        dependsOn(fullJar)
    }
}