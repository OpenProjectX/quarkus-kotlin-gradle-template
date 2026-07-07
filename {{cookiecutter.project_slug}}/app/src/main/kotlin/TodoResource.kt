package {{ cookiecutter.group_id }}.app

import io.smallrye.mutiny.Uni
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.net.URI

/**
 * Fully reactive CRUD endpoints for [Todo] — web to DB without blocking.
 * Every method returns a [Uni].
 */
@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class TodoResource(
    private val service: TodoService
) {

    @GET
    fun list(): Uni<List<Todo>> = service.findAll()

    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id: Long): Uni<Response> =
        service.findById(id).map { todo ->
            if (todo == null) Response.status(Response.Status.NOT_FOUND).build()
            else Response.ok(todo).build()
        }

    @POST
    fun create(todo: Todo): Uni<Response> =
        service.create(todo).map { created ->
            Response.created(URI.create("/todos/${created.id}")).entity(created).build()
        }

    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, changes: Todo): Uni<Response> =
        service.update(id, changes).map { updated ->
            if (updated == null) Response.status(Response.Status.NOT_FOUND).build()
            else Response.ok(updated).build()
        }

    @DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: Long): Uni<Response> =
        service.deleteById(id).map { deleted ->
            if (deleted) Response.noContent().build()
            else Response.status(Response.Status.NOT_FOUND).build()
        }
}
