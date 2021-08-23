package com.freelapp.tasksflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Tests {
    @Test
    fun `4 tasks emits 25, 50, 75, 100`() = runBlockingTest {
        val result = tasksFlow {
            delay(125L)
            task { delay(100L) }
            task { delay(100L) }
            task { delay(100L) }
            task { delay(100L) }
            delay(50L)
        }
            .toList()
        assert(result.size == 4)
        assertEquals(listOf(25, 50, 75, 100), result)
    }

    @Test
    fun `1 task emits 100`() = runBlockingTest {
        val result = tasksFlow {
            task { delay(100L) }
            delay(50L)
        }
            .toList()
        assert(result.size == 1)
        assertEquals(listOf(100), result)
    }

    @Test
    fun `exception inside task is thrown to enclosing flow`() = runBlockingTest {
        val result = tasksFlow {
            task { delay(100L) }
            task { delay(100L) }
            task { throw IllegalStateException("Error") }
            task { delay(100L) }
            task { delay(100L) }
            delay(50L)
        }
            .catch { assert(it is IllegalStateException && it.message == "Error") }
            .toList()
        assert(result.size == 2)
        assertEquals(listOf(20, 40), result)
    }

    @Test
    fun `single task emits 100`() = runBlockingTest {
        val result = tasksFlow {
            task { delay(100L) }
        }
            .toList()
        assert(result.size == 1)
        assertEquals(listOf(100), result)
    }

    @Test
    fun `no tasks emits nothing`() = runBlockingTest {
        val result = tasksFlow {}.toList()
        assert(result.isEmpty())
        assertEquals(listOf(), result)
    }
}
