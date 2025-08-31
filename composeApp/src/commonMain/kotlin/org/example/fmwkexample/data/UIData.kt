package org.example.fmwkexample.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.backend.attr.AttrBase
import org.example.backend.attr.IAttrData
import org.example.backend.attr.RWX
import org.example.backend.flow.mapState

/**
 * Helps you use [AttrBase]s correctly in the front-end.
 * Has helper methods suitable for calling stateless composables.
 *
 * @see collectAsState
 */
class UIData<V, M, D : IAttrData<V, M>, A : AttrBase<V, M, D>> private constructor(
    private val _data: D,
    private val attr: A
) {
    /**
     * The underlying attribute's data if readable, otherwise `null`.
     * This should always be transformed into some UI-appropriate data type so that your stateless composables aren't tightly coupled to attribute types.
     */
    val data = if (RWX.R in _data.mode) { _data } else null

    /**
     * Get a block that will send input to the attribute when it is called.
     *
     * @param input should use its argument to call one of the attribute's input methods.
     * In the block, `this` is the attribute.
     * @return the block that sends input to the attribute, or `null` if the attribute isn't writable.
     */
    fun <T> writer(input: A.(T) -> Unit): ((T) -> Unit)? {
        return if (RWX.W in _data.mode) {
            { attr.input(it) }
        } else null
    }

    /**
     * Get a block that will send input to the attribute when it is called.
     *
     * @param input should call one of the attribute's input methods.
     * In the block, `this` is the attribute.
     * @return the block that sends input to the attribute, or `null` if the attribute isn't writable.
     */
    fun writer(input: A.() -> Unit): (() -> Unit)? {
        return if (RWX.W in _data.mode) {
            { attr.input() }
        } else null
    }

    /**
     * A block that executes the attribute, or `null` if the attribute isn't executable.
     */
    val executer = if (RWX.X in _data.mode) {
        { attr.execute() }
    } else null

    companion object {
        /**
         * Efficiently collects this attr's data in the front-end as [State].
         * The returned [UIData] is probably not directly useful, but can be easily transformed into whatever data your UI needs.
         */
        @Composable
        fun <V, M, D : IAttrData<V, M>, A : AttrBase<V, M, D>> A.collectAsState(): State<UIData<V, M, D, A>> {
            return dataFlow.mapState {
                UIData(it, this@collectAsState)
            }.collectAsStateWithLifecycle()
        }
    }
}
