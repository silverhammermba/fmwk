package org.example.fmwkexample

import android.content.Intent

actual fun sendText(
    title: String,
    subject: String,
    text: String
) {
    val context = globalState?.context ?: return
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            title
        )
    )
}
