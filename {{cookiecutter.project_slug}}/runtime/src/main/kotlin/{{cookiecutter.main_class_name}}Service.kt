package {{ cookiecutter.group_id }}.runtime

import jakarta.enterprise.context.ApplicationScoped

/**
 * A CDI bean provided by the extension. Consumers inject it directly.
 *
 * It is made an unremovable bean by a `@BuildStep` in the deployment module
 * (see [{{ cookiecutter.main_class_name }}Processor]), which is how an extension
 * contributes beans to applications that depend on it. It in turn injects the
 * extension's type-safe [{{ cookiecutter.main_class_name }}Config].
 */
@ApplicationScoped
class {{ cookiecutter.main_class_name }}Service(
    private val config: {{ cookiecutter.main_class_name }}Config
) {
    fun greet(name: String): String = "${config.message()}, $name!"
}
