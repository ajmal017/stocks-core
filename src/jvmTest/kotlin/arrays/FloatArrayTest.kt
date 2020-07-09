package org.cerion.stocks.core.arrays

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.overlays.SimpleMovingAverage
import org.junit.Assert.assertEquals
import org.junit.Test

class FloatArrayTest : TestBase() {

    val arr: FloatArray = priceList.close

    @Test
    fun sma_callsSimpleMovingAverage() {
        val arr1 = SimpleMovingAverage(13).eval(arr)
        val arr2 = arr.sma(13)

        val map = mutableMapOf<Char, Int>()
        val test = map.maxBy { it.value }
        assertEquals("all elements should be equal", arr1.last.toDouble(), arr2.last.toDouble(), 0.00000001)
    }

    @Test
    fun slope() {
        assertEqual(0.0, arr.slope(5, 0), "slope position 0")
        assertEqual(-55.8, arr.slope(5, 1), "slope position 1")
        assertEqual(-2.35, arr.slope(5, 4), "slope position 4")
        assertEqual(15.57, arr.slope(5, 5), "slope position 5")
        assertEqual(-2.72, arr.slope(5, arr.size - 1), "slope position last") // stockcharts verified
    }

    @Test
    fun regressionLine() {
        assertEqual(1455.22, arr.regressionLinePoint(5, 0), "regressionLine position 0")
        assertEqual(1483.12, arr.regressionLinePoint(5, 1), "regressionLine position 1")
        assertEqual(1422.68, arr.regressionLinePoint(5, 4), "regressionLine position 4")
        assertEqual(1405.24, arr.regressionLinePoint(5, 5), "regressionLine position 5")
        assertEqual(2063.35, arr.regressionLinePoint(5, arr.size - 1), "regressionLine position last")
    }

    @Test
    fun zeroLengthArrays() {
        val arr = FloatArray(0)
        arr.ema(20)
    }

    @Test
    fun sum() {
        assertEquals(1455.22, arr.sum(0, 0).toDouble(), 0.005)
        assertEquals(2854.64, arr.sum(0, 1).toDouble(), 0.005)
        assertEquals(8698.38, arr.sum(5, 10).toDouble(), 0.005)
    }

    @Test
    fun std() {
        val period = 10
        val std = arr.std(period)

        assertEqual(0.0, std.first, "standard deviation position 0")
        assertEqual(27.90, std[1], "standard deviation position 1")
        assertEqual(23.34, std[period - 1], "standard deviation position p-1")
        assertEqual(23.33, std[period], "standard deviation position p")
        assertEqual(20.78, std.last, "standard deviation position last")
    }

    @Test
    fun correlation() {
        var corr = arr.correlation(arr)
        assertEqual(1.0, corr, "correlation self")
        assertEquals("correlation self", 1.0, corr.toDouble(), 0.000001)

        val volume = TestBase.priceList.volume
        val high = TestBase.priceList.high

        corr = arr.correlation(high)
        assertEquals("correlation high", 0.999527, corr.toDouble(), 0.000001)

        corr = arr.correlation(volume)
        assertEquals("correlation volume", 0.069388, corr.toDouble(), 0.000001)
    }
}
