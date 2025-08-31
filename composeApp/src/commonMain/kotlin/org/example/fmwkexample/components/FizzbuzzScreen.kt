package org.example.fmwkexample.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.backend.myfeature.Foobar
import org.example.fmwkexample.data.UIData.Companion.collectAsState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FizzbuzzScreen(
    model: Foobar,
    modifier: Modifier = Modifier
) {
    val options by model.options.collectAsState()

    val list = options.data?.list?.mapIndexed { idx, text ->
        text to options.writer { input(idx) }
    }

    FizzbuzzScreen(
        options = list ?: listOf(),
        modifier = modifier,
    )
}

@Composable
fun FizzbuzzScreen(
    options: List<Pair<String, (() -> Unit)?>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            options.forEach { item ->
                Button(
                    onClick = item.second ?: {},
                    modifier = Modifier.widthIn(min = 250.dp),
                    enabled = item.second != null
                ) {
                    Text(item.first)
                }
            }
        }
    }
}

@Preview
@Composable
fun StartOrderPreview() {
    MaterialTheme {
        FizzbuzzScreen(
            options = listOf(
                "First option" to {},
                "Second option" to null
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}
