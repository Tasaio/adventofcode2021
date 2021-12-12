plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src/main/kotlin")
        }
    }

    wrapper {
        gradleVersion = "7.3"
    }
}

dependencies {
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
}
