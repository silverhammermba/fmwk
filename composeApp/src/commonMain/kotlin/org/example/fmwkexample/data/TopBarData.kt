package org.example.fmwkexample.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.example.backend.attr.Attr
import org.example.backend.attr.AttrData
import org.example.backend.myfeature.Foobar
import org.example.fmwkexample.data.UIData.Companion.collectAsState

data class TopBarData(
    val name: String,
    val back: (() -> Unit)?
)

@Composable
fun <M, N> topBarData(title: Attr<String, M, AttrData<String, M>>, back: Attr<Unit, N, AttrData<Unit, N>>?): TopBarData {
    val title by title.collectAsState()
    val backAction = back?.collectAsState()?.value?.writer { input(Unit) }

    return TopBarData(title.data?.value ?: "", backAction)
}

@Composable
fun topBarData(model: Foobar): TopBarData {
    return topBarData<Unit, Unit>(model.title, null)
}
