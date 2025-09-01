package org.example.fmwkexample

import platform.UIKit.UIActivityViewController

actual fun sendText(
    title: String,
    subject: String,
    text: String
) {
    val mainController = globalState?.mainViewController ?: return
    val activityController = UIActivityViewController(
        activityItems = listOf(title, subject, text),
        applicationActivities = null
    )
    mainController.presentViewController(
        viewControllerToPresent = activityController,
        animated = true,
        completion = null
    )
}
