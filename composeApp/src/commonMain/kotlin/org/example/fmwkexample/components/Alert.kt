package org.example.fmwkexample.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.example.backend.alert.AlertData

@Composable
fun Alert(
    alert: AlertData,
    modifier: Modifier = Modifier
) {
    Alert(
        title = alert.title,
        body = alert.body,
        cancel = alert.cancel?.let { it.text to it.action },
        destructive = alert.destructive?.let { it.text to it.action },
        positive = alert.positive?.let { it.text to it.action },
        modifier = modifier
    )
}

@Composable
fun Alert(
    title: String?,
    body: String?,
    cancel: Pair<String, () -> Unit>?,
    destructive: Pair<String, () -> Unit>?,
    positive: Pair<String, () -> Unit>?,
    modifier: Modifier = Modifier
) {
    Dialog({ cancel?.second?.invoke() }) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let { Text(it) }
                body?.let { Text(it) }
                cancel?.let { Button(it.second) { Text(it.first) } }
                destructive?.let { Button(it.second) { Text(it.first) } }
                positive?.let { Button(it.second) { Text(it.first) } }
            }
        }
    }
}
