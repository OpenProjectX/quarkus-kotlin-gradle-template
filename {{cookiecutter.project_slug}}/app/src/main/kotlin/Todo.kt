package {{ cookiecutter.group_id }}.app

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Reactive Panache entity (active-record). The companion object exposes the
 * reactive query/persist methods, each returning a Mutiny [io.smallrye.mutiny.Uni].
 *
 * The schema is owned by Liquibase (see db/changelog), so the id uses IDENTITY
 * to line up with the `autoIncrement` column and Hibernate does no DDL.
 */
@Entity
@Table(name = "todo")
class Todo : PanacheEntityBase {

    companion object : PanacheCompanion<Todo>

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var title: String? = null

    var completed: Boolean = false

    var priority: Int = 0
}
