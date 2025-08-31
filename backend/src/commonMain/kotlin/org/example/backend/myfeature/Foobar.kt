package org.example.backend.myfeature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.backend.attr.Attr
import org.example.backend.attr.AttrData
import org.example.backend.attr.ListAttr
import org.example.backend.attr.ListData
import org.example.backend.attr.RWX

class Foobar internal constructor(
    coroutineScope: CoroutineScope,
) {
    val title = Attr(AttrData("Hello, world!", RWX.R))

    val options = ListAttr(
        ListData(
            null,
            emptyList<String>(),
            RWX.R
        ),
        write = { println("Selected $it") }
    )

    init {
        coroutineScope.launch {
            options.data = options.data.copy(
                list = listOf("Chips", "Ice Cream"),
                mode = RWX.RW
            )
        }
    }
}
