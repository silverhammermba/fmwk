package org.example.backend.myfeature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.backend.alert.AlertChoice
import org.example.backend.alert.AlertData
import org.example.backend.router.Destination
import org.example.backend.router.IRouter
import org.example.backend.router.Route
import kotlin.time.Duration.Companion.seconds

internal class MyFeatureDirector(
    private val router: IRouter,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    var startRoute: Route<*>? = null

    fun start() {
        coroutineScope.launch {
            delay(1.seconds)
            val model = Foobar(
                coroutineScope,
                ::someAlert,
                null,
                ::moreFoobar
            )
            startRoute = router.push(Destination.Fizzbuzz(model))
        }
    }

    fun moreFoobar() {
        val model = Foobar(
            coroutineScope,
            ::someAlert,
            { router.pop() },
            ::moreFoobar
        )
        router.push(Destination.Fizzbuzz(model))
    }

    fun someAlert() {
        val alert = AlertData(
            "Hello!",
            "This is an alert.",
            AlertChoice("Nice") { router.hideAlert() },
        )
        router.showAlert(alert)
    }
}
