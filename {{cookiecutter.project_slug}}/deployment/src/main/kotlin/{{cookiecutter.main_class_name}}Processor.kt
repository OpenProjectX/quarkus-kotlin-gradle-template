package {{ cookiecutter.group_id }}.deployment

import io.quarkus.arc.deployment.AdditionalBeanBuildItem
import io.quarkus.deployment.annotations.BuildStep
import io.quarkus.deployment.builditem.FeatureBuildItem
import {{ cookiecutter.group_id }}.runtime.{{ cookiecutter.main_class_name }}Service

/**
 * Build-time processor — the "autoconfigure" of the extension.
 *
 * Each `@BuildStep` method runs once during Quarkus augmentation (build time)
 * and produces typed *BuildItems* that the framework consumes. This is what
 * replaces Spring's runtime `@Conditional` auto-configuration: the decisions
 * are made at build time and baked into bytecode (which is why Quarkus starts
 * fast and compiles to native).
 */
class {{ cookiecutter.main_class_name }}Processor {

    private val feature = "{{ cookiecutter.project_slug }}"

    /** Registers the extension in the "Installed features" banner at startup. */
    @BuildStep
    fun feature(): FeatureBuildItem = FeatureBuildItem(feature)

    /**
     * Contributes [{{ cookiecutter.main_class_name }}Service] as a CDI bean to every
     * application that uses this extension, and keeps it even if unused.
     */
    @BuildStep
    fun registerBeans(): AdditionalBeanBuildItem =
        AdditionalBeanBuildItem.unremovableOf({{ cookiecutter.main_class_name }}Service::class.java)
}
