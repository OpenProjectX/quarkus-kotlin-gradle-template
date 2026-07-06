// ===========================================================================
// Extension DEPLOYMENT module  ==  the "autoconfigure".
//
// Contains the build-time wiring: classes with `@BuildStep` methods that run
// during Quarkus augmentation (at BUILD time, not app startup) to register
// beans, read config, generate bytecode, etc. NONE of this code ships in the
// final application. Its artifactId MUST be `<runtime-artifactId>-deployment`.
// ===========================================================================
plugins {
    id("buildsrc.convention.kotlin-jvm")
    // kapt runs the Quarkus extension annotation processor (see below), which
    // generates META-INF/quarkus-build-steps.list so Quarkus can find the
    // @BuildStep methods. This mirrors the reference template's use of
    // `kotlin-kapt` + spring-boot-configuration-processor in the autoconfigure module.
    `kotlin-kapt`
}

dependencies {
    val quarkusBom = enforcedPlatform("io.quarkus.platform:quarkus-bom:${libs.versions.quarkus.get()}")
    implementation(quarkusBom)
    kapt(quarkusBom)

    // Build-time APIs: BuildStep, BuildItems, and the ArC deployment helpers.
    implementation("io.quarkus:quarkus-core-deployment")
    implementation("io.quarkus:quarkus-arc-deployment")

    // The deployment module must depend on the runtime module it augments.
    implementation(project(":{{ cookiecutter.project_slug }}"))

    // Generates the build-steps descriptor from @BuildStep annotations.
    kapt("io.quarkus:quarkus-extension-processor")
}
