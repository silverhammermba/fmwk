package org.example.fmwkexample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.backend.alert.AlertData
import org.example.backend.event.EventData
import org.example.backend.router.Path
import org.example.backend.router.Route
import org.example.backend.router.Router

/**
 * A passthrough view model for the front-end NavHostController to get state and models from the back-end [Router].
 */
class AppViewModel(val router: Router) : ViewModel() {
    /** @see [Router.path] */
    val path: StateFlow<Path> = router.path

    /** @see [Router.alert] */
    val alert: StateFlow<AlertData?> = router.alert

    /** @see [Router.events] */
    val events: SharedFlow<EventData> = router.events

    /** @see [Router.start] */
    fun start() {
        router.start()
    }

    /** @see [Router.getModel] */
    fun <L, R : Route<L>> getModel(route: R): L? {
        return router.getModel(route)
    }
}
