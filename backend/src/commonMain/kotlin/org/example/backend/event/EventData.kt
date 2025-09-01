package org.example.backend.event

/**
 * Events that are sent to the UI for it to handle on its own.
 * Things like starting new activities or linking the user to something outside of the app.
 *
 * The back-end cannot "undo" an event, unlike popping a route or hiding an alert.
 */
sealed interface EventData {
    /** Share text with another app */
    data class SendText(
        val title: String,
        val subject: String,
        val text: String
    ) : EventData
}
