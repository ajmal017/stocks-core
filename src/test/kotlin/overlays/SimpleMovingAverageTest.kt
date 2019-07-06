package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleMovingAverageTest : TestBase() {

    val arr = priceList.close

    @Test
    fun sma_1() {
        val sma = arr.sma(1)
        assertEquals("Unexpected test arrays length", 4025, sma.size)

        for (i in 0 until sma.size)
            assertEqual(arr.get(i).toDouble(), sma.get(i), "position $i")
    }

    @Test
    fun sma_2() {
        val sma = arr.sma(2)

        assertEquals("Unexpected test arrays length", 4025, sma.size)
        assertEqual(1455.22, sma.get(0), "position 0")
        assertEqual(1427.32, sma.get(1), "position 1")
        assertEqual(1429.40, sma.get(sma.size / 2), "position " + sma.size / 2)
        assertEqual(2053.65, sma.get(sma.size - 1), "position last")
    }

    @Test
    fun sma_20() {
        val sma = arr.sma(20)

        assertEquals("Unexpected test arrays length", 4025, sma.size)
        assertEqual(1455.22, sma.get(0), "position 0")
        assertEqual(1427.32, sma.get(1), "position 1")
        assertEqual(1473.51, sma.get(sma.size / 2), "position " + sma.size / 2)
        assertEqual(2050.38, sma.get(sma.size - 1), "position last")
    }

    @Test
    fun sma_200() {
        val sma = arr.sma(200)

        assertEquals("Unexpected test arrays length", 4025, sma.size.toLong())
        assertEqual(1455.22, sma.get(0), "position 0")
        assertEqual(1427.32, sma.get(1), "position 1")
        assertEqual(1490.95, sma.get(sma.size / 2), "position " + sma.size / 2)
        assertEqual(2061.15, sma.get(sma.size - 1), "position last")
    }

    @Test
    fun sma_usesHighestAverage() {
        val sma20 = arr.sma(20)
        val sma100 = arr.sma(100)
        val sma200 = arr.sma(200)

        for (i in 0..19)
            assertEqual(sma20.get(i).toDouble(), sma100.get(i), "20 and 100 position $i")

        for (i in 0..19)
            assertEqual(sma20.get(i).toDouble(), sma200.get(i), "20 and 200 position $i")

        for (i in 0..99)
            assertEqual(sma100.get(i).toDouble(), sma200.get(i), "100 and 200 position $i")
    }
}