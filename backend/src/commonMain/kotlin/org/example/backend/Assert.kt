package org.example.backend

// no built-in assert for KMP
fun assert(condition: Boolean, message: String? = null) {
    if (!condition) {
        message?.let { println(it) }
        throw AssertionError()
    }
}
