package {{ cookiecutter.group_id }}.app

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import {{ cookiecutter.group_id }}.runtime.{{ cookiecutter.main_class_name }}Service

/**
 * Example endpoint proving the extension works end-to-end: the app injects a
 * bean that the extension contributed via a build step.
 *
 *   GET /greeting/world  ->  "Hello from {{ cookiecutter.project_name }}, world!"
 */
@Path("/greeting")
class {{ cookiecutter.main_class_name }}Resource(
    private val service: {{ cookiecutter.main_class_name }}Service
) {

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    fun greet(@PathParam("name") name: String): String = service.greet(name)
}
