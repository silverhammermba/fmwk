package org.example.backend.attr

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RwxTest {

    @Test
    fun readableModes() {
        assertFalse(RWX.R in RWX.NONE)
        assertFalse(RWX.R in RWX.X)
        assertFalse(RWX.R in RWX.W)
        assertFalse(RWX.R in RWX.WX)
        assertTrue(RWX.R in RWX.R)
        assertTrue(RWX.R in RWX.RX)
        assertTrue(RWX.R in RWX.RW)
        assertTrue(RWX.R in RWX.ALL)
    }

    @Test
    fun writableModes() {
        assertFalse(RWX.W in RWX.NONE)
        assertFalse(RWX.W in RWX.X)
        assertTrue(RWX.W in RWX.W)
        assertTrue(RWX.W in RWX.WX)
        assertFalse(RWX.W in RWX.R)
        assertFalse(RWX.W in RWX.RX)
        assertTrue(RWX.W in RWX.RW)
        assertTrue(RWX.W in RWX.ALL)
    }

    @Test
    fun executableModes() {
        assertFalse(RWX.X in RWX.NONE)
        assertTrue(RWX.X in RWX.X)
        assertFalse(RWX.X in RWX.W)
        assertTrue(RWX.X in RWX.WX)
        assertFalse(RWX.X in RWX.R)
        assertTrue(RWX.X in RWX.RX)
        assertFalse(RWX.X in RWX.RW)
        assertTrue(RWX.X in RWX.ALL)
    }

    @Test
    fun readableAndWritableModes() {
        assertFalse(RWX.RW in RWX.NONE)
        assertFalse(RWX.RW in RWX.X)
        assertFalse(RWX.RW in RWX.W)
        assertFalse(RWX.RW in RWX.WX)
        assertFalse(RWX.RW in RWX.R)
        assertFalse(RWX.RW in RWX.RX)
        assertTrue(RWX.RW in RWX.RW)
        assertTrue(RWX.RW in RWX.ALL)
    }

    @Test
    fun readableAndExecutableModes() {
        assertFalse(RWX.RX in RWX.NONE)
        assertFalse(RWX.RX in RWX.X)
        assertFalse(RWX.RX in RWX.W)
        assertFalse(RWX.RX in RWX.WX)
        assertFalse(RWX.RX in RWX.R)
        assertTrue(RWX.RX in RWX.RX)
        assertFalse(RWX.RX in RWX.RW)
        assertTrue(RWX.RX in RWX.ALL)
    }

    @Test
    fun writableAndExecutableModes() {
        assertFalse(RWX.WX in RWX.NONE)
        assertFalse(RWX.WX in RWX.X)
        assertFalse(RWX.WX in RWX.W)
        assertTrue(RWX.WX in RWX.WX)
        assertFalse(RWX.WX in RWX.R)
        assertFalse(RWX.WX in RWX.RX)
        assertFalse(RWX.WX in RWX.RW)
        assertTrue(RWX.WX in RWX.ALL)
    }

    @Test
    fun readableAndWritableAndExecutableModes() {
        assertFalse(RWX.ALL in RWX.NONE)
        assertFalse(RWX.ALL in RWX.X)
        assertFalse(RWX.ALL in RWX.W)
        assertFalse(RWX.ALL in RWX.WX)
        assertFalse(RWX.ALL in RWX.R)
        assertFalse(RWX.ALL in RWX.RX)
        assertFalse(RWX.ALL in RWX.RW)
        assertTrue(RWX.ALL in RWX.ALL)
    }

    @Test
    fun plus() {
        assertEquals(RWX.R, RWX.R + RWX.R)
        assertEquals(RWX.RW, RWX.W + RWX.R)
        assertEquals(RWX.RW, RWX.RW + RWX.R)
        assertEquals(RWX.R, RWX.NONE + RWX.R)
        assertEquals(RWX.RX, RWX.X + RWX.R)
        assertEquals(RWX.RX, RWX.RX + RWX.R)
        assertEquals(RWX.ALL, RWX.WX + RWX.R)
        assertEquals(RWX.ALL, RWX.ALL + RWX.R)

        assertEquals(RWX.RW, RWX.R + RWX.W)
        assertEquals(RWX.W, RWX.W + RWX.W)
        assertEquals(RWX.RW, RWX.RW + RWX.W)
        assertEquals(RWX.W, RWX.NONE + RWX.W)
        assertEquals(RWX.WX, RWX.X + RWX.W)
        assertEquals(RWX.ALL, RWX.RX + RWX.W)
        assertEquals(RWX.WX, RWX.WX + RWX.W)
        assertEquals(RWX.ALL, RWX.ALL + RWX.W)

        assertEquals(RWX.RX, RWX.R + RWX.X)
        assertEquals(RWX.WX, RWX.W + RWX.X)
        assertEquals(RWX.ALL, RWX.RW + RWX.X)
        assertEquals(RWX.X, RWX.NONE + RWX.X)
        assertEquals(RWX.X, RWX.X + RWX.X)
        assertEquals(RWX.RX, RWX.RX + RWX.X)
        assertEquals(RWX.WX, RWX.WX + RWX.X)
        assertEquals(RWX.ALL, RWX.ALL + RWX.X)

        assertEquals(RWX.RW, RWX.R + RWX.RW)
        assertEquals(RWX.RW, RWX.W + RWX.RW)
        assertEquals(RWX.RW, RWX.RW + RWX.RW)
        assertEquals(RWX.RW, RWX.NONE + RWX.RW)
        assertEquals(RWX.ALL, RWX.X + RWX.RW)
        assertEquals(RWX.ALL, RWX.RX + RWX.RW)
        assertEquals(RWX.ALL, RWX.WX + RWX.RW)
        assertEquals(RWX.ALL, RWX.ALL + RWX.RW)

        assertEquals(RWX.RX, RWX.R + RWX.RX)
        assertEquals(RWX.ALL, RWX.W + RWX.RX)
        assertEquals(RWX.ALL, RWX.RW + RWX.RX)
        assertEquals(RWX.RX, RWX.NONE + RWX.RX)
        assertEquals(RWX.RX, RWX.X + RWX.RX)
        assertEquals(RWX.RX, RWX.RX + RWX.RX)
        assertEquals(RWX.ALL, RWX.WX + RWX.RX)
        assertEquals(RWX.ALL, RWX.ALL + RWX.RX)

        assertEquals(RWX.ALL, RWX.R + RWX.WX)
        assertEquals(RWX.WX, RWX.W + RWX.WX)
        assertEquals(RWX.ALL, RWX.RW + RWX.WX)
        assertEquals(RWX.WX, RWX.NONE + RWX.WX)
        assertEquals(RWX.WX, RWX.X + RWX.WX)
        assertEquals(RWX.ALL, RWX.RX + RWX.WX)
        assertEquals(RWX.WX, RWX.WX + RWX.WX)
        assertEquals(RWX.ALL, RWX.ALL + RWX.WX)

        assertEquals(RWX.ALL, RWX.R + RWX.ALL)
        assertEquals(RWX.ALL, RWX.W + RWX.ALL)
        assertEquals(RWX.ALL, RWX.RW + RWX.ALL)
        assertEquals(RWX.ALL, RWX.NONE + RWX.ALL)
        assertEquals(RWX.ALL, RWX.X + RWX.ALL)
        assertEquals(RWX.ALL, RWX.RX + RWX.ALL)
        assertEquals(RWX.ALL, RWX.WX + RWX.ALL)
        assertEquals(RWX.ALL, RWX.ALL + RWX.ALL)

        assertEquals(RWX.R, RWX.R + RWX.NONE)
        assertEquals(RWX.W, RWX.W + RWX.NONE)
        assertEquals(RWX.RW, RWX.RW + RWX.NONE)
        assertEquals(RWX.NONE, RWX.NONE + RWX.NONE)
        assertEquals(RWX.X, RWX.X + RWX.NONE)
        assertEquals(RWX.RX, RWX.RX + RWX.NONE)
        assertEquals(RWX.WX, RWX.WX + RWX.NONE)
        assertEquals(RWX.ALL, RWX.ALL + RWX.NONE)
    }

    @Test
    fun minus() {
        assertEquals(RWX.NONE, RWX.ALL - RWX.ALL)
        assertEquals(RWX.NONE, RWX.WX - RWX.ALL)
        assertEquals(RWX.NONE, RWX.RX - RWX.ALL)
        assertEquals(RWX.NONE, RWX.X - RWX.ALL)
        assertEquals(RWX.NONE, RWX.RW - RWX.ALL)
        assertEquals(RWX.NONE, RWX.W - RWX.ALL)
        assertEquals(RWX.NONE, RWX.R - RWX.ALL)
        assertEquals(RWX.NONE, RWX.NONE - RWX.ALL)

        assertEquals(RWX.W, RWX.ALL - RWX.RX)
        assertEquals(RWX.W, RWX.WX - RWX.RX)
        assertEquals(RWX.NONE, RWX.RX - RWX.RX)
        assertEquals(RWX.NONE, RWX.X - RWX.RX)
        assertEquals(RWX.W, RWX.RW - RWX.RX)
        assertEquals(RWX.W, RWX.W - RWX.RX)
        assertEquals(RWX.NONE, RWX.R - RWX.RX)
        assertEquals(RWX.NONE, RWX.NONE - RWX.RX)

        assertEquals(RWX.RW, RWX.ALL - RWX.X)
        assertEquals(RWX.W, RWX.WX - RWX.X)
        assertEquals(RWX.R, RWX.RX - RWX.X)
        assertEquals(RWX.NONE, RWX.X - RWX.X)
        assertEquals(RWX.RW, RWX.RW - RWX.X)
        assertEquals(RWX.W, RWX.W - RWX.X)
        assertEquals(RWX.R, RWX.R - RWX.X)
        assertEquals(RWX.NONE, RWX.NONE - RWX.X)

        assertEquals(RWX.RX, RWX.ALL - RWX.W)
        assertEquals(RWX.X, RWX.WX - RWX.W)
        assertEquals(RWX.RX, RWX.RX - RWX.W)
        assertEquals(RWX.X, RWX.X - RWX.W)
        assertEquals(RWX.R, RWX.RW - RWX.W)
        assertEquals(RWX.NONE, RWX.W - RWX.W)
        assertEquals(RWX.R, RWX.R - RWX.W)
        assertEquals(RWX.NONE, RWX.NONE - RWX.W)

        assertEquals(RWX.X, RWX.ALL - RWX.RW)
        assertEquals(RWX.X, RWX.WX - RWX.RW)
        assertEquals(RWX.X, RWX.RX - RWX.RW)
        assertEquals(RWX.X, RWX.X - RWX.RW)
        assertEquals(RWX.NONE, RWX.RW - RWX.RW)
        assertEquals(RWX.NONE, RWX.W - RWX.RW)
        assertEquals(RWX.NONE, RWX.R - RWX.RW)
        assertEquals(RWX.NONE, RWX.NONE - RWX.RW)

        assertEquals(RWX.ALL, RWX.ALL - RWX.NONE)
        assertEquals(RWX.WX, RWX.WX - RWX.NONE)
        assertEquals(RWX.RX, RWX.RX - RWX.NONE)
        assertEquals(RWX.X, RWX.X - RWX.NONE)
        assertEquals(RWX.RW, RWX.RW - RWX.NONE)
        assertEquals(RWX.W, RWX.W - RWX.NONE)
        assertEquals(RWX.R, RWX.R - RWX.NONE)
        assertEquals(RWX.NONE, RWX.NONE - RWX.NONE)

        assertEquals(RWX.WX, RWX.ALL - RWX.R)
        assertEquals(RWX.WX, RWX.WX - RWX.R)
        assertEquals(RWX.X, RWX.RX - RWX.R)
        assertEquals(RWX.X, RWX.X - RWX.R)
        assertEquals(RWX.W, RWX.RW - RWX.R)
        assertEquals(RWX.W, RWX.W - RWX.R)
        assertEquals(RWX.NONE, RWX.R - RWX.R)
        assertEquals(RWX.NONE, RWX.NONE - RWX.R)
    }
}
