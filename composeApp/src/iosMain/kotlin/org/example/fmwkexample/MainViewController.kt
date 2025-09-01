package org.example.fmwkexample

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/**
 * Front-end-only global state.
 * Be cautious about what is added here.
 *
 * All fields must remain valid for the entire application lifetime.
 */
data class GlobalState(
    val mainViewController: UIViewController
)

var globalState: GlobalState? = null

fun MainViewController(): UIViewController {
    val controller = ComposeUIViewController {
        App()
    }
    globalState = GlobalState(controller)
    return controller
}
