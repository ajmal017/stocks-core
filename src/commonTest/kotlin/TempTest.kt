package org.cerion.stocks.core

import kotlin.test.Test
import kotlin.test.assertEquals

class TempTest {

    @Test
    fun test() = runTest {
        val file = readResourceFile("a.txt")
        assertEquals("hello", file)
    }
}