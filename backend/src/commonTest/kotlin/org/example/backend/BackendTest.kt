package org.example.backend

import kotlin.test.Test
import kotlin.test.assertEquals

class BackendTest {
    @Test
    fun returnsString() {
        assertEquals("Hello, world!", Backend.backend())
    }
}
