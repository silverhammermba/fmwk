package org.example.fmwkexample

import org.example.backend.event.EventData

/**
 * Decide what to do with each UI event
 */
suspend fun routeEvent(data: EventData) {
    when (data) {
        is EventData.SendText -> sendText(data.title, data.subject, data.text)
    }
}

/** Open share dialog to send text to another app */
expect fun sendText(title: String, subject: String, text: String)
