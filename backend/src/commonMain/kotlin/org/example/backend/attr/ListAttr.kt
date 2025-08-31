package org.example.backend.attr

/**
 * Attr data for a list with 0 or 1 selected items where the front-end can change the selection.
 * See [ListAttr] for usage.
 *
 * This is an example of defining a more specific type than [AttrData] to add other read-only data to the attr.
 */
@ConsistentCopyVisibility
data class ListData<T : Any, M> internal constructor(
    /**
     * The index of the currently selected item in [list] (if any).
     * The front-end should not assume that this index will be in bounds.
     */
    override val value: Int?,
    /**
     * The list of items for the front-end.
     * Should be shown on the front-end if and only if mode is readable, just like [value] and [metadata].
     */
    val list: List<T>,
    override val mode: RWX,
    override val metadata: M,
) : IAttrData<Int?, M> {
    /**
     * The currently selected item in [list] (if selected and if the list is readable).
     */
    val item: T?
        get() = if (RWX.R in mode && value != null && value >= 0 && value < list.size) {
            list[value]
        } else {
            null
        }
}

/**
 * Convenience constructor for [ListData] when you don't need any use-case-specific metadata.
 */
internal fun <T : Any> ListData(
    value: Int?,
    list: List<T>,
    mode: RWX,
) = ListData(value, list, mode, Unit)

/**
 * Attr representing a list with 0 or 1 selected items where the front-end can change the selection.
 *
 * This is an example of when you want to define a much more rich attr type than [Attr]: this includes additional read-only data (the list) and convenience functions for ensuring the I/O value is consistent with the list.
 */
class ListAttr<T : Any, M> internal constructor(
    data: ListData<T, M>,
    execute: ((ListData<T, M>) -> Unit)? = null,
    write: ((Int?) -> Unit)? = null,
) : AttrBase<Int?, M, ListData<T, M>>(data, execute, write) {
    /**
     * Call from the front-end whenever the user selects a value in the list or clears the selection.
     */
    fun input(index: Int?) {
        if (index == null || (index >= 0 && index < data.list.size)) {
            write(index)
        } else {
            println("Ignoring out-of-bounds selection: $index / ${data.list.size}")
        }
    }

    /**
     * Convenience function for the back-end to safely change the selected index by item.
     */
    internal fun setItem(item: T) {
        val index = data.list.indexOf(item)
        val value = if (index < 0) null else index
        data = data.copy(value = value)
    }
}
