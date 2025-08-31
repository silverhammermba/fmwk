package org.example.backend.attr

/**
 * The simplest attr data, covering most use-cases: a generic value with no other data.
 *
 * Most commonly used by [Attr], but can also be reused in other simple attrs like [BoolAttr].
 */
@ConsistentCopyVisibility
data class AttrData<V, M> internal constructor(
    override val value: V,
    override val mode: RWX,
    override val metadata: M
): IAttrData<V, M>

/**
 * Convenience constructor for [AttrData] when you don't need any use-case-specific metadata.
 */
internal fun <V> AttrData(
    value: V,
    mode: RWX
) = AttrData(value, mode, Unit)

/**
 * The simplest attr, covering most use-cases: the front-end can directly [input] new values and no special input validation is needed.
 */
class Attr<V, M, D: IAttrData<V, M>> internal constructor(
    data: D,
    execute: ((D) -> Unit)? = null,
    write: ((V) -> Unit)? = null,
) : AttrBase<V, M, D>(data, execute, write) {
    /**
     * Call from the front-end whenever the user inputs a new value.
     */
    fun input(value: V) = write(value)
}
