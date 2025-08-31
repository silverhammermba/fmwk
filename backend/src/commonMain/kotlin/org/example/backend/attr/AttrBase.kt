package org.example.backend.attr

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * All data used for dynamic front-end I/O controlled by the back-end should conform to this interface.
 * This establishes a baseline that all front-end components adhere to when binding to the back-end.
 *
 * This should always be presented within some [AttrBase] instance to manage the actual I/O.
 *
 * Modifying this interface should be extremely rare as it will require updating every single part of your app where front- and back-end meet.
 * But you might do this to establish a new baseline for front-end functionality which is testable in the back-end.
 * For example, if you want to ensure that every UI element has a back-end-controlled accessibility string for screen readers, you could add a new `val` here for that string.
 */
interface IAttrData<V, M> {
    /**
     * The primary data for the front-end.
     * Also the only data that accept input (if [mode] is writable).
     *
     * Implementations of this interface may provide other read-only fields.
     * Instance-specific read-only data can use [metadata].
     */
    val value: V

    /**
     * Indicates how this instance should be used by the front-end:
     * * R: [value] (and any other data such as [metadata]) should be displayed to the user.
     *   This allows the back-end to dynamically show/hide data on the front-end.
     * * W: the front-end may indicate that input of a new [value] is allowed.
     *   This allows the back-end to dynamically enable/disable input.
     * * X: the front-end may allow a secondary (0 parameter) action for this attr.
     *   For example, this could be triggered by a long press on the corresponding UI component.
     *
     * W/X are "may"s because [AttrBase] ensures that front-end inputs are ignored whenever the mode prohibits input.
     * And conversely it is always safe for a read-only UI component to bind to W/X data.
     */
    val mode: RWX

    /**
     * Use-case-specific read-only data.
     * Usually just [Unit] since you don't need metadata or the implementation of this interface provides more specific read-only fields (e.g. [ListAttr]).
     * Common examples where this is useful:
     * * you need to display input validation errors alongside the [value] on the UI
     * * this attr allows input and also has a dynamic read-only label/description
     */
    val metadata: M
}

/**
 * All dynamic front-end I/O controlled by the back-end should be decomposed into instances of this type.
 *
 * @param initialData the initial data which will be visible to the front-end prior to any dynamic back-end updates.
 * @param _execute callback for the secondary (0 parameter) front-end action.
 * This is called with the current [data] (only if the `mode` is executable).
 * Call [execute] to use this.
 * @param _write callback for the front-end inputting a new `value` for [data] (only if the `mode` is writable).
 * Call [write] from a subclass to use this.
 * This callback **must** be able to effect a change to [data] or cause some other side effect using its parameter.
 * If the callback never changes [data] or ignores its parameter, you are probably abusing attrs.
 * The one exception is if [V] is [Unit]: since that is a singleton, this callback sometimes only triggers side effects in that case.
 */
abstract class AttrBase<V, M, D: IAttrData<V, M>>(
    initialData: D,
    private val _execute: ((D) -> Unit)? = null,
    private val _write: ((V) -> Unit)? = null,
) {
    // private mutable state since dataFlow and data should be sufficient for all use-cases
    private val mutableDataFlow = MutableStateFlow(initialData)

    /**
     * Collect this in the front-end to asynchronously observe all back-end changes to this attr.
     *
     * This can also be collected in the back-end, for example to automatically update another attr whenever this attr's data change.
     */
    val dataFlow: StateFlow<D>
        get() = mutableDataFlow

    /**
     * Set this to asynchronously emit data to anyone collecting [dataFlow] (usually the front-end).
     * Getting this gets the latest value, but be wary of race conditions when separate coroutines are setting this.
     *
     * Practically, [D] is always a data class, so you can perform a partial update using the generated `copy` method.
     *
     *     attr.data = attr.data.copy(foo = bar)
     */
    internal var data: D
        get() = mutableDataFlow.value
        set(newData) {
            mutableDataFlow.value = newData
        }

    /**
     * Call this from a subclass whenever the user inputs a new `value` for [data].
     * This is a no-op if the [data] `mode` is not writable.
     * Your subclass method should perform any other input validation before calling this.
     *
     * This is `protected` to ensure that your subclass-specific input validation cannot be skipped.
     *
     * **Never** call this from a back-end initiated operation unless you are explicitly emulating front-end input, for example in tests.
     * Usually back-end operations directly update [data] with a new `value`.
     */
    protected fun write(value: V) {
        _write?.let {
            if (RWX.W in mutableDataFlow.value.mode) {
                it(value)
            }
        }
    }

    /**
     * Call this whenever the user triggers the secondary (0 parameter) action on the front-end.
     * This is a no-op if the [data] `mode` is not executable.
     *
     * **Never** call this from a back-end initiated operation unless you are explicitly emulating front-end input, for example in tests.
     */
    fun execute() {
        _execute?.let {
            val value = mutableDataFlow.value
            if (RWX.X in value.mode) {
                it(value)
            }
        }
    }
}
