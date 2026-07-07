package {{ cookiecutter.group_id }}.app

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 * Reactive service layer — every method returns a [Uni] and never blocks.
 *  - [WithSession]     opens a read-only reactive session (queries).
 *  - [WithTransaction] opens a session + transaction, committing on success.
 */
@ApplicationScoped
class TodoService {

    @WithSession
    fun findAll(): Uni<List<Todo>> = Todo.listAll()

    @WithSession
    fun findById(id: Long): Uni<Todo?> = Todo.findById(id)

    @WithTransaction
    fun create(todo: Todo): Uni<Todo> {
        todo.id = null // let the database generate the id
        return todo.persist()
    }

    @WithTransaction
    fun update(id: Long, changes: Todo): Uni<Todo?> =
        Todo.findById(id).flatMap { existing ->
            if (existing == null) {
                Uni.createFrom().nullItem()
            } else {
                existing.title = changes.title
                existing.completed = changes.completed
                existing.priority = changes.priority
                // Managed entity is flushed on commit; just return it.
                Uni.createFrom().item(existing)
            }
        }

    @WithTransaction
    fun deleteById(id: Long): Uni<Boolean> = Todo.deleteById(id)
}
