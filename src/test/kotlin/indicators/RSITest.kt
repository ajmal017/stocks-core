package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test

class RSITest : TestBase() {

    @Test
    fun eval() {
        val rsi = RSI(14).eval(priceList)

        assertEqual(50.0, rsi.get(0), "p0")
        assertEqual(33.33, rsi.get(1), "p1")
        assertEqual(34.39, rsi.get(2), "p2")
        assertEqual(47.57, rsi.last, "last")
    }

}