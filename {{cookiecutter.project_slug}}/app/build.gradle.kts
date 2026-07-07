// Example Quarkus application that CONSUMES the extension, exactly as an end
// user would. It depends only on the runtime module (the "starter"); Quarkus
// pulls in the matching `-deployment` artifact automatically at build time.
plugins {
    id("buildsrc.convention.quarkus-kotlin")
}

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${libs.versions.quarkus.get()}"))

    // The extension under development (its runtime module).
    implementation(project(":{{ cookiecutter.project_slug }}"))

    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-arc")
    // Needed to read application.yaml (Quarkus reads .properties out of the box).
    implementation("io.quarkus:quarkus-config-yaml")

    // Quinoa builds the React app in the sibling `webui` module and serves it
    // from this app (see quarkus.quinoa.* in application.yaml). Version is not
    // managed by quarkus-bom, so it comes from the version catalog.
    implementation(libs.quarkusQuinoa)

    testImplementation("io.quarkus:quarkus-junit5")
}
