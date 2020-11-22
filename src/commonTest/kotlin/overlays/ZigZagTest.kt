package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class ZigZagTest : TestBase() {

    @Test
    fun eval() = runPriceTest {
        val arr = ZigZag(20.0).eval(it)

        assertEqual(it.high[0], arr[0], "zigzag start")
        assertEqual(it.low[307], arr[307], "zigzag first low")
        assertEqual(it.high[349], arr[349], "zigzag first high")
        assertEqual(it.low[2957], arr[2957], "zigzag last low")
        assertEqual(it.high[3868], arr[3868], "zigzag last high")
        assertEqual(it.low.last, arr.last, "zigzag end")
    }
}