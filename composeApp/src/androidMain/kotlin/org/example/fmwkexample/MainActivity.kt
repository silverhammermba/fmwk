package org.example.fmwkexample

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

/**
 * Front-end-only global state.
 * Be cautious about what is added here.
 *
 * All fields must remain valid for the entire application lifetime.
 */
data class GlobalState(
    val context: Context
)

/**
 * Intentionally "leaked" global state for the front-end.
 * Since the entire app is run within one activity, this only leaks objects that would be in memory anyway.
 */
@SuppressLint("StaticFieldLeak")
var globalState: GlobalState? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            globalState = GlobalState(LocalContext.current)
            App()
        }
    }
}
