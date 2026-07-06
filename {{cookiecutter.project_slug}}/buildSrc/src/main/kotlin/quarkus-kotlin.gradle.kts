// Convention plugin for a Quarkus *application* written in Kotlin.
// This is the Quarkus analog of the reference template's `spring-kotlin` plugin:
// it wires up the Quarkus Gradle plugin and Kotlin all-open so that CDI beans,
// JAX-RS resources and JPA entities are open for Quarkus/Hibernate proxying.
package buildsrc.convention

plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("io.quarkus")
    kotlin("plugin.allopen")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}
