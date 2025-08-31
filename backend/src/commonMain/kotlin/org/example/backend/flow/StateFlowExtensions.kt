package org.example.backend.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Get a [StateFlow] from any [Flow] by providing a [getValue] callback for computing whatever the current value of the flow should be.
 *
 * Usually you don't want to construct this manually.
 * Call [mapState] instead.
 *
 * From [StackOverflow](https://stackoverflow.com/a/72783757/503402).
 */
class ComputedStateFlow<T>(
    private val flow: Flow<T>,
    private val getValue: () -> T
) : StateFlow<T> {
    override val replayCache: List<T> get() = listOf(value)
    override val value: T get() = getValue()

    override suspend fun collect(collector: FlowCollector<T>) = coroutineScope {
        flow.stateIn(this).collect(collector)
    }
}

/**
 * Returns a [StateFlow] containing the results of applying the given [transform] function to each value of the original flow.
 *
 * Preferred over [map]+[stateIn] since it doesn't require a coroutine scope.
 * Use [stateIn] only if you need your transform to suspend or if you didn't have a [StateFlow] to begin with.
 * A suspending transform requires [stateIn] because [StateFlow.value] is non-suspending.
 * You need a coroutine scope to be collecting the original flow if you want to get the mapped value without suspending.
 */
fun <T, R> StateFlow<T>.mapState(transform: (T) -> R): StateFlow<R> {
    return ComputedStateFlow(map(transform)) { transform(value) }
}
