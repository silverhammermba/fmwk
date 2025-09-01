package org.example.backend.myfeature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.backend.attr.Attr
import org.example.backend.attr.AttrData
import org.example.backend.attr.ListAttr
import org.example.backend.attr.ListData
import org.example.backend.attr.RWX
import kotlin.time.Duration.Companion.seconds

class Foobar internal constructor(
    coroutineScope: CoroutineScope,
    alert: () -> Unit,
    back: (() -> Unit)?,
    push: () -> Unit,
    share: () -> Unit,
) {
    val title = Attr(AttrData("Hello, world!", RWX.R))

    val back = Attr(
        AttrData(Unit, if (back == null) RWX.NONE else RWX.RW),
        write = { back?.invoke() }
    )

    val options = ListAttr(
        ListData(
            null,
            emptyList<String>(),
            RWX.R
        ),
        write = {
            if (it == 0) {
                alert()
            } else if (it == 1) {
                push()
            } else if (it == 2) {
                share()
            }
        }
    )

    init {
        coroutineScope.launch {
            options.data = options.data.copy(
                list = listOf("Alert", "Push", "Share"),
                mode = RWX.RW
            )
            delay(1.seconds)
            title.data = title.data.copy(value = "Dynamic!")
        }
    }
}
