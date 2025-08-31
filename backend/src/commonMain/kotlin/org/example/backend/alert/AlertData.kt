package org.example.backend.alert

import org.example.backend.assert

/**
 * A choice that the user can make when presented with a representation of [AlertData].
 *
 * @param text a very short, localized description of the choice
 * @param action triggered by the front-end when the choice is made.
 * This should almost always result in hiding the associated [AlertData] in addition to any other side effects.
 */
@ConsistentCopyVisibility
data class AlertChoice internal constructor(val text: String, val action: () -> Unit)

/**
 * The front-end must block all other input when this is sent from the back-end until one of the provided choices is made.
 * One of the [AlertChoice] parameters **must** be provided and have an action that hides the alert, or else the user will be soft-locked.
 *
 * @param title a short, localized description of why the user is being alerted.
 * If `null`, you **must** provide a [body].
 * @param body a localized description of the details of the alert.
 * If `null`, you **must** provide a [title].
 * @param cancel should be set to the choice with minimal side effects, basically ignoring the alert, if possible.
 * The front-end may allow the user to tap/swipe away the alert to select this option.
 * @param destructive should be set to the choice that is hard to undo, for example because of data loss, if any.
 * @param positive should be set to the choice that generally agrees or proceeds (based on the [title]/[body]), if any.
 */
@ConsistentCopyVisibility
data class AlertData internal constructor(
    val title: String? = null,
    val body: String? = null,
    val cancel: AlertChoice? = null,
    val destructive: AlertChoice? = null,
    val positive: AlertChoice? = null
) {
    init {
        assert(
            title != null || body != null,
            "Constructed alert with no localized description."
        )
        assert(
            cancel != null || destructive != null || positive != null,
            "Constructed alert with no choices."
        )
    }
}
