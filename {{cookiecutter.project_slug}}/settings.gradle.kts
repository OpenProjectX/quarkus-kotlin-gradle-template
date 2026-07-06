import java.io.File

pluginManagement {
    repositories {
        val isCi = System.getenv().containsKey("CI") ||
                System.getenv().containsKey("GITHUB_ACTIONS") ||
                System.getenv().containsKey("JENKINS_HOME")

        if (!isCi) {
            maven(url = "https://mirrors.tencent.com/nexus/repository/maven-public/")
            maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        }

        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        val isCi = System.getenv().containsKey("CI") ||
                System.getenv().containsKey("GITHUB_ACTIONS") ||
                System.getenv().containsKey("JENKINS_HOME")

        if (!isCi) {
            maven(url = "https://mirrors.tencent.com/nexus/repository/maven-public/")
            maven(url = "https://maven.aliyun.com/repository/public/")
        }

        mavenCentral()
    }
}

rootProject.name = "{{ cookiecutter.project_slug }}"

// ---------------------------------------------------------------------------
// Auto-discover subprojects by scanning for build files.
//  - `build.gradle[.kts]`         -> project path derived from its directory
//  - `<name>.gradle[.kts]`        -> project named `<name>`, dir = the file's parent
// This lets the extension modules use descriptive file names so their Gradle
// project name (and therefore Maven artifactId) is exactly the module name:
//   runtime/{{ cookiecutter.project_slug }}.gradle.kts             -> :{{ cookiecutter.project_slug }}
//   deployment/{{ cookiecutter.project_slug }}-deployment.gradle.kts -> :{{ cookiecutter.project_slug }}-deployment
// ---------------------------------------------------------------------------
val excludeProjects: String? by settings

val buildFiles = fileTree(rootDir) {
    val excludes = excludeProjects?.split(",")
    include("**/*.gradle", "**/*.gradle.kts")
    exclude(
        "build",
        "**/gradle",
        "settings.gradle",
        "settings.gradle.kts",
        "buildSrc",
        "/build.gradle",
        "/build.gradle.kts",
        ".*",
        "out"
    )
    if (!excludes.isNullOrEmpty()) {
        exclude(excludes)
    }
}

val rootDirPath = rootDir.absolutePath + File.separator
buildFiles.forEach { buildFile ->
    val isDefaultName = buildFile.name.startsWith("build.gradle")
    val isKotlin = buildFile.name.endsWith(".kts")

    if (isDefaultName) {
        val buildFilePath = buildFile.parentFile.absolutePath
        val projectPath = buildFilePath
            .replace(rootDirPath, "")
            .replace(File.separator, ":")

        println("Adding project $projectPath")
        include(projectPath)
    } else {
        val projectName = if (isKotlin) {
            buildFile.name.removeSuffix(".gradle.kts")
        } else {
            buildFile.name.removeSuffix(".gradle")
        }

        val projectPath = ":$projectName"
        println("Adding project $projectPath")
        include(projectPath)

        val project = findProject(projectPath)
        project?.name = projectName
        project?.projectDir = buildFile.parentFile
        project?.buildFileName = buildFile.name
    }
}

gradle.extra["isCi"] = System.getenv().containsKey("CI") ||
        System.getenv().containsKey("GITHUB_ACTIONS") ||
        System.getenv().containsKey("JENKINS_HOME")
