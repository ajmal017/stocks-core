package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.junit.Test


class ZigZagTest : TestBase() {

    @Test
    fun eval() {
        val arr = ZigZag(20.0).eval(priceList)

        assertEqual(priceList.high[0], arr[0], "zigzag start")
        assertEqual(priceList.low[307], arr[307], "zigzag first low")
        assertEqual(priceList.high[349], arr[349], "zigzag first high")
        assertEqual(priceList.low[2957], arr[2957], "zigzag last low")
        assertEqual(priceList.high[3868], arr[3868], "zigzag last high")
        assertEqual(priceList.low[size - 1], arr[size - 1], "zigzag end")
    }
}