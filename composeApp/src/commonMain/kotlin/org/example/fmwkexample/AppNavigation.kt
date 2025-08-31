package org.example.fmwkexample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.backend.event.EventData
import org.example.backend.router.Path
import org.example.backend.router.Route
import org.example.backend.router.Router
import org.example.fmwkexample.components.Alert
import org.example.fmwkexample.components.FizzbuzzScreen
import org.example.fmwkexample.components.MyTopBar
import org.example.fmwkexample.data.TopBarData
import org.example.fmwkexample.data.topBarData

// TODO: deep links

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppNavigation(
    viewModel: AppViewModel = viewModel {
        AppViewModel(Router())
    },
    navController: NavHostController = rememberNavController()
) {
    LaunchedEffect(Unit) {
        viewModel.start()
    }

    val path by viewModel.path.collectAsState()
    val alert by viewModel.alert.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is EventData.SendText -> sendText(event.title, event.subject, event.text)
            }
        }
    }

    var lastPath by remember { mutableStateOf(Path(listOf())) }
    if (path.path != lastPath.path) {
        var diffIndex = 0
        while (path.path.size > diffIndex && lastPath.path.size > diffIndex && path.path[diffIndex] == lastPath.path[diffIndex]) {
            diffIndex += 1
        }
        (0 until (lastPath.path.size - diffIndex)).forEach { _ ->
            navController.popBackStack()
        }
        (diffIndex until path.path.size).forEach { idx ->
            navController.navigate(path.path[idx])
        }

        lastPath = path
    }

    alert?.let {
        Alert(it)
    }

    @Composable
    fun AlertBackHandler(onBack: () -> Unit) {
        val shownAlert = alert
        BackHandler {
            if (shownAlert == null) {
                onBack()
            } else {
                shownAlert.cancel?.action?.invoke()
            }
        }
    }

    val topBarState = remember { mutableStateOf<TopBarData?>(null) }

    @Composable
    fun TopBar(value: TopBarData?) {
        topBarState.value = value
        AlertBackHandler {
            value?.back?.invoke()
        }
    }

    Scaffold(
        topBar = {
            topBarState.value?.let {
                MyTopBar(it)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Empty,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Route.Empty> {
                TopBar(null)
            }

            composable<Route.Fizzbuzz> { entry ->
                val route: Route.Fizzbuzz = entry.toRoute()
                val model = viewModel.getModel(route) ?: return@composable

                TopBar(topBarData(model))

                FizzbuzzScreen(
                    model = model,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

fun sendText(
    title: String,
    subject: String,
    text: String
) {
    // TODO: fix share activity
//    val intent = Intent(Intent.ACTION_SEND).apply {
//        type = "text/plain"
//        putExtra(Intent.EXTRA_SUBJECT, subject)
//        putExtra(Intent.EXTRA_TEXT, text)
//    }
//    context.startActivity(
//        Intent.createChooser(
//            intent,
//            title
//        )
//    )
}
