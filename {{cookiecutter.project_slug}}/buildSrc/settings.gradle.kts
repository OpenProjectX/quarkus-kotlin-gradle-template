dependencyResolutionManagement {

    // Use mirrors + Maven Central for resolving dependencies in the shared build logic (`buildSrc`) project.
    @Suppress("UnstableApiUsage")
    repositories {
        val isCi = System.getenv().containsKey("CI") ||
                System.getenv().containsKey("GITHUB_ACTIONS") ||
                System.getenv().containsKey("JENKINS_HOME")
        if (!isCi) {
            maven(url = "https://mirrors.tencent.com/nexus/repository/maven-public/")
        }
        mavenCentral()
        // The Quarkus Gradle plugin marker (io.quarkus:io.quarkus.gradle.plugin)
        // is published to the Gradle Plugin Portal, not Maven Central.
        gradlePluginPortal()
    }

    // Reuse the version catalog from the main build.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "buildSrc"
