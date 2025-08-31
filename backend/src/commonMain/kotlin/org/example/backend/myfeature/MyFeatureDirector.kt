package org.example.backend.myfeature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.backend.router.Destination
import org.example.backend.router.IRouter
import org.example.backend.router.Route

internal class MyFeatureDirector(
    private val router: IRouter,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    var startRoute: Route<*>? = null

    fun start() {
        coroutineScope.launch {
            val model = Foobar(coroutineScope)
            startRoute = router.push(Destination.Fizzbuzz(model))
        }
    }
}
