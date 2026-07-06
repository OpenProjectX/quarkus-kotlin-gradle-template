package {{ cookiecutter.group_id }}.runtime

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

/**
 * A CDI bean provided by the extension. Consumers inject it directly.
 *
 * It is made an unremovable bean by a `@BuildStep` in the deployment module
 * (see [{{ cookiecutter.main_class_name }}Processor]), which is how an extension
 * contributes beans to applications that depend on it.
 *
 * The greeting prefix is configurable via MicroProfile Config — set
 * `{{ cookiecutter.project_slug }}.message` in application.yaml / application.properties.
 */
@ApplicationScoped
class {{ cookiecutter.main_class_name }}Service(
    @ConfigProperty(name = "{{ cookiecutter.project_slug }}.message", defaultValue = "Hello from {{ cookiecutter.project_name }}")
    private val message: String
) {
    fun greet(name: String): String = "$message, $name!"
}
