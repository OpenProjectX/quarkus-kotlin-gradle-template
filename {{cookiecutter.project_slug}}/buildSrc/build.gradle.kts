plugins {
    // The Kotlin DSL plugin provides a convenient way to develop convention plugins.
    // Convention plugins are located in `src/main/kotlin`, with the file extension `.gradle.kts`,
    // and are applied in the project's `build.gradle.kts` files as required.
    `kotlin-dsl`
}

kotlin {
    jvmToolchain({{ cookiecutter.java_version }})
}

dependencies {
    // Put the Kotlin + Quarkus Gradle plugins on the buildSrc classpath so that
    // convention plugins can apply them by id (e.g. `id("io.quarkus")`).
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.kotlinAllopenPlugin)
    implementation(libs.quarkusGradlePlugin)
}
