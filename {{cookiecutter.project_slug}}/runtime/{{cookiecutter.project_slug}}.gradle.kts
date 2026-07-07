// ===========================================================================
// Extension RUNTIME module  ==  the "starter" a user depends on.
//
// This is what consumers add to their build. It contains the beans, config
// classes and runtime code that ship inside the final application. The
// `io.quarkus.extension` plugin generates the extension descriptor
// (META-INF/quarkus-extension.properties) that points at the matching
// `-deployment` artifact, and builds a Jandex index of these classes.
// ===========================================================================
plugins {
    id("buildsrc.convention.kotlin-jvm")
    // The io.quarkus.extension plugin wires the quarkus-extension-processor onto
    // the `annotationProcessor` configuration, but Kotlin ignores that — it needs
    // kapt. Running the processor here generates META-INF/quarkus-config-roots.list
    // so the @ConfigRoot config classes in this module are discovered.
    `kotlin-kapt`
    alias(libs.plugins.quarkusExtension)
}

// The deployment module isn't named literally "deployment", so point the
// extension plugin at it (it must be `<runtime-artifactId>-deployment`).
quarkusExtension {
    deploymentModule = "{{ cookiecutter.project_slug }}-deployment"
}

dependencies {
    val quarkusBom = enforcedPlatform("io.quarkus.platform:quarkus-bom:${libs.versions.quarkus.get()}")
    implementation(quarkusBom)
    kapt(quarkusBom)

    // quarkus-arc brings CDI + quarkus-core; add other runtime extensions your
    // extension builds on (e.g. quarkus-rest, quarkus-hibernate-orm) here.
    implementation("io.quarkus:quarkus-arc")

    // Re-export the framework-agnostic core library to consumers.
    api(project(":core"))

    // Generates the extension metadata (incl. quarkus-config-roots.list) from
    // annotations in this runtime module. Required for @ConfigRoot discovery.
    kapt("io.quarkus:quarkus-extension-processor")
}
