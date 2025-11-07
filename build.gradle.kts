plugins {
    kotlin("jvm") version "2.0.21"
    id("java")
}

group = "io.github.seoksoon"
version = "1.0.0"
description = "Heart of Fortress - Paper 1.21.4 PvP Plugin (Kotlin)"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        filteringCharset = "UTF-8"
    }

    jar {
        archiveBaseName.set("HeartOfFortress")

        // ✅ Kotlin 런타임을 플러그인 jar에 포함
        from({
            configurations.runtimeClasspath.get()
                .filter {
                    it.name.contains("kotlin-stdlib") ||
                            it.name.contains("kotlin-reflect")
                }
                .map { if (it.isDirectory) it else zipTree(it) }
        })

        // 중복 방지
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}