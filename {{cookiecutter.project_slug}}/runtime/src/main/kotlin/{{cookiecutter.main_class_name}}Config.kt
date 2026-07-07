package {{ cookiecutter.group_id }}.runtime

import io.quarkus.runtime.annotations.ConfigPhase
import io.quarkus.runtime.annotations.ConfigRoot
import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithDefault

/**
 * Type-safe configuration for the extension — the Quarkus equivalent of Spring's
 * `@ConfigurationProperties`. Bound from properties prefixed with
 * `{{ cookiecutter.project_slug }}.` (e.g. `{{ cookiecutter.project_slug }}.message=...`).
 *
 * In an extension, `@ConfigMapping` MUST be paired with `@ConfigRoot`: that pair
 * is what makes Quarkus auto-expose an instance to the configuration phase and
 * register it as an injectable bean. For this discovery to work the extension
 * annotation processor must run on this (runtime) module — see the `kapt`
 * dependency in `runtime/{{ cookiecutter.project_slug }}.gradle.kts`, which
 * generates META-INF/quarkus-config-roots.list.
 */
@ConfigMapping(prefix = "{{ cookiecutter.project_slug }}")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
interface {{ cookiecutter.main_class_name }}Config {

    /** Greeting prefix. Override in application.yaml / application.properties. */
    @WithDefault("Hello from {{ cookiecutter.project_name }}")
    fun message(): String
}
