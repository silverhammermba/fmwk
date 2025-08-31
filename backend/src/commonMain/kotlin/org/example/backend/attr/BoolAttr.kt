package org.example.backend.attr

/**
 * An attr where the data value is specifically a Boolean.
 *
 * This is an example where it can be helpful to use a more specific type than [Attr]: since it is non-generic we can provide a helpful [toggle] method for the front-end.
 */
class BoolAttr<M> internal constructor(
    data: AttrData<Boolean, M>,
    execute: ((AttrData<Boolean, M>) -> Unit)? = null,
    write: ((Boolean) -> Unit)? = null
) : AttrBase<Boolean, M, AttrData<Boolean, M>>(data, execute, write) {
    /**
     * Call from the front-end whenever the user inputs a new value.
     */
    fun input(value: Boolean) = write(value)

    /**
     * Call from the front-end when the user wants to toggle the value.
     */
    fun toggle() = write(!data.value)
}
