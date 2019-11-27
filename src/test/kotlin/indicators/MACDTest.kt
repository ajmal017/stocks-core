package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.indicators.MACD
import org.junit.Test

class MACDTest : TestBase() {

    @Test
    fun macd_defaults() {
        val arr = MACD().eval(priceList)

        val last = arr.size - 1
        assertEqual(-1.69, arr[last], "last")
        assertEqual(1.84, arr.hist(last), "hist")
        assertEqual(-3.54, arr.signal(last), "signal")
    }
}