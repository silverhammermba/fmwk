package org.example.backend.attr

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class AttrBaseTest {
    private data class TestData(
        override val value: Int,
        override val mode: RWX,
        override val metadata: Int = 0
    ) : IAttrData<Int, Int>

    private class TestAttr(
        data: TestData,
        execute: ((TestData) -> Unit)? = null,
        write: ((Int) -> Unit)? = null
    ) : AttrBase<Int, Int, TestData>(data, execute, write) {
        fun input(value: Int) = write(value)
    }

    @Test
    fun getSetData() {
        val initialData = TestData(3, RWX.R)
        val attribute = TestAttr(initialData)

        // should match initial data
        assertEquals(initialData, attribute.data)

        val testData = listOf(
            TestData(1, RWX.RW),
            TestData(2, RWX.W),
            TestData(3, RWX.NONE)
        )

        // can get/set data like a normal var
        testData.forEach {
            attribute.data = it
            assertEquals(it, attribute.data)
        }
    }

    @Test
    fun collectFlow() = runTest {
        val initialData = TestData(3, RWX.R)
        val attr = TestAttr(initialData)

        var collected1: TestData? = null
        val job1 = launch {
            attr.dataFlow.collect {
                collected1 = it
            }
        }

        // should collect initial data
        advanceUntilIdle()
        assertEquals(initialData, collected1)

        // should collect a change via setter
        val newData = TestData(4, RWX.RW)
        attr.data = newData
        advanceUntilIdle()
        assertEquals(newData, collected1)

        // test multiple collectors
        var collected2: TestData? = null

        val job2 = launch {
            attr.dataFlow.collect {
                collected2 = it
            }
        }

        // should skip straight to latest value (no initialData)
        advanceUntilIdle()
        assertEquals(newData, collected2)

        // test change with multiple collectors
        val newerData = TestData(0, RWX.NONE)
        attr.data = newerData
        advanceUntilIdle()
        assertEquals(newerData, collected1)
        assertEquals(newerData, collected2)

        // stop collecting
        job2.cancel()

        val newestData = TestData(1, RWX.W)
        attr.data = newestData
        advanceUntilIdle()
        assertEquals(newestData, collected1)
        // hasn't updated
        assertEquals(newerData, collected2)

        // cancel remaining collectors so the test scope can complete
        job1.cancel()
    }

    @Test
    fun writeChecksMode() {
        var currentValue = 0

        val attribute = TestAttr(
            TestData(7, RWX.R),
            execute = { fail("should not execute") },
            write = { currentValue = it }
        )

        // constructor does not call write
        assertEquals(0, currentValue)

        val testCases = listOf(
            Pair(0, RWX.NONE),
            Pair(0, RWX.RX),
            Pair(1, RWX.W),
            Pair(2, RWX.RW),
            Pair(3, RWX.ALL),
        )

        // try to change the value for each mode
        testCases.forEach { (expected, mode) ->
            attribute.data = attribute.data.copy(mode = mode)
            attribute.input(currentValue + 1)

            assertEquals(expected, currentValue)
        }
    }

    @Test
    fun executeChecksMode() {
        var executions = 0

        val attribute = TestAttr(
            TestData(7, RWX.ALL),
            execute = { executions += 1 },
            write = { fail("should not write") }
        )

        // constructor does not call execute
        assertEquals(0, executions)

        val testCases = listOf(
            Pair(0, RWX.NONE),
            Pair(0, RWX.R),
            Pair(0, RWX.W),
            Pair(1, RWX.X),
            Pair(1, RWX.RW),
            Pair(2, RWX.ALL),
        )

        // try to execute for each mode
        testCases.forEach { (expected, mode) ->
            attribute.data = attribute.data.copy(mode = mode)
            attribute.execute()

            assertEquals(expected, executions)
        }
    }
}
