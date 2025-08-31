package org.example.backend.router

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.backend.alert.AlertData
import org.example.backend.assert
import org.example.backend.event.EventData
import org.example.backend.myfeature.MyFeatureDirector
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

data class Path(val path: List<Route<*>>)

internal interface IRouter {
    fun push(dest: Destination<*>): Route<*>

    fun pop(to: Route<*>? = null)

    fun showAlert(alert: AlertData)

    fun hideAlert()

    fun sendEvent(event: EventData)
}

class Router {
    @OptIn(ExperimentalAtomicApi::class)
    internal class InternalRouter : IRouter {
        sealed interface RouterOp {
            data class Push(val dest: Destination<*>, val route: Route<*>) : RouterOp

            data class Pop(val to: Route<*>?) : RouterOp
        }

        private val routerOperations = MutableSharedFlow<RouterOp>(replay = 1000)

        private val path = MutableStateFlow(Path(listOf()))

        private val alert = MutableStateFlow<AlertData?>(null)

        private val events = MutableSharedFlow<EventData>(replay = 1000)

        val pathFlow: StateFlow<Path> = path
        val alertFlow: StateFlow<AlertData?> = alert
        val eventsFlow: SharedFlow<EventData> = events

        private val routeId = AtomicInt(0)

        var modelRegistry = HashMap<Int, Any>()

        init {
            CoroutineScope(Dispatchers.Main).launch {
                routerOperations.collect {
                    when (it) {
                        is RouterOp.Push -> unsafePush(it.dest, it.route)
                        is RouterOp.Pop -> unsafePop(it.to)
                    }
                }
            }
        }

        private fun unsafePush(dest: Destination<*>, route: Route<*>) {
            assert(route !is Route.Empty)
            modelRegistry[route.id] = dest.model
            val newRoutes = path.value.path.toMutableList()
            newRoutes.add(route)
            path.value = Path(path = newRoutes)
        }

        private fun unsafePop(to: Route<*>? = null) {
            val newRoutes = path.value.path.toMutableList()
            var targetSize = newRoutes.size - 1
            to?.let {
                targetSize = newRoutes.lastIndexOf(it) + 1
                assert(targetSize > 0, "Route not in path, ignoring pop to $to")
                if (targetSize <= 0) return
                if (targetSize == newRoutes.size) return
            }
            while (newRoutes.size > targetSize) {
                val poppedRoute = newRoutes.removeLastOrNull() ?: break
                assert(poppedRoute !is Route.Empty, "Router bug? Path contained Route.Empty")
                modelRegistry.remove(poppedRoute.id)
            }
            path.value = Path(newRoutes)
        }

        override fun push(dest: Destination<*>): Route<*> {
            val route = dest.route(routeId.addAndFetch(1))
            assert(
                routerOperations.tryEmit(RouterOp.Push(dest, route)),
                "Ignored push: router buffer overflow!"
            )
            return route
        }

        override fun pop(to: Route<*>?) {
            assert(
                routerOperations.tryEmit(RouterOp.Pop(to)),
                "Ignored pop: router buffer overflow!"
            )
        }

        override fun showAlert(alert: AlertData) {
            this.alert.value = alert
        }

        override fun hideAlert() {
            alert.value = null
        }

        override fun sendEvent(event: EventData) {
            assert(
                events.tryEmit(event),
                "Ignored event: router buffer overflow!"
            )
        }
    }

    private val internalRouter = InternalRouter()

    val path: StateFlow<Path> = internalRouter.pathFlow

    val alert: StateFlow<AlertData?> = internalRouter.alertFlow

    val events: SharedFlow<EventData> = internalRouter.eventsFlow

    fun start() {
        MyFeatureDirector(internalRouter).start()
    }

    fun <M, R : Route<M>> getModel(route: R): M? {
        @Suppress("UNCHECKED_CAST")
        return internalRouter.modelRegistry[route.id] as M?
    }
}
