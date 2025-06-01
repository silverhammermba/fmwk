package org.example.fmwkexample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform