package org.example.backend.attr

/**
 * Similar to file access modes, a value with separate flags for:
 * * R: readable
 * * W: writable
 * * X: executable
 *
 * The exact meaning of these depends on its usage.
 * See [IAttrData.mode] for an example.
 */
enum class RWX(private val value: Int) {
    NONE(0b000),
    X(0b001),
    W(0b010),
    WX(0b011),
    R(0b100),
    RX(0b101),
    RW(0b110),
    ALL(0b111);

    operator fun contains(mode: RWX): Boolean {
        return value.and(mode.value) == mode.value
    }

    operator fun minus(mode: RWX): RWX {
        return fromValue(value.and(mode.value.inv()))
    }

    operator fun plus(mode: RWX): RWX {
        return fromValue(value.or(mode.value))
    }

    companion object {
        private fun fromValue(value: Int): RWX {
            val masked = value.and(ALL.value)
            return entries.first { it.value == masked }
        }
    }
}
