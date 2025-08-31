package org.example.backend.router

import kotlinx.serialization.Serializable
import org.example.backend.myfeature.Foobar

/**
 * Uniquely identifies a [Destination] and the [id] of the destination's model.
 *
 * @param M the type of this route's destination's model
 */
sealed interface Route<M : Any> {
    val id: Int

    /**
     * Placeholder used only by the front-end to represent an empty back stack.
     *
     * No [Destination] should use this.
     */
    @Serializable
    data object Empty : Route<Unit> {
        override val id = -1
    }

    // below is the list of each route in the app

    @Serializable
    data class Fizzbuzz(override val id: Int) : Route<Foobar>
}

/**
 * A node in the navigation graph.
 * When the back-end navigates to this node, the front-end displays its [model].
 *
 * Each [Route] (other than [Route.Empty]) should have a destination (to be pushed).
 * No two destinations should use the same [Route] since it's redundant (same route means showing the same [model] to the front-end).
 *
 * @param M the type of this destination's [model]
 */
internal sealed interface Destination<M : Any> {
    /**
     * Construct a [Route] for this destination.
     * The front-end will use it to retrieve the [model].
     */
    fun route(id: Int): Route<M>

    /**
     * The public data for the front-end to use when displaying this destination.
     * To keep all significant application logic in the back-end, the model type should:
     *
     * 1. Make all `public` properties immutable
     * 2. Make all `public` methods return `Unit`
     * 3. Apply these rules to every type it exposes to the front-end
     *
     * Mutable data can be represented by [kotlinx.coroutines.flow.StateFlow].
     * [org.example.backend.attr.AttrBase] is especially useful for this.
     */
    val model: M

    // below is the list of each destination in the app

    data class Fizzbuzz(override val model: Foobar): Destination<Foobar> {
        override fun route(id: Int) = Route.Fizzbuzz(id)
    }
}
