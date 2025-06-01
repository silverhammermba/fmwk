package org.example.fmwkexample

import org.example.backend.Backend

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "${Backend.backend()} from ${platform.name}!"
    }
}
