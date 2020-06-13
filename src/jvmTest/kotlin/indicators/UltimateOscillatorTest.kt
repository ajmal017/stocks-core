package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import org.junit.Test


class UltimateOscillatorTest : TestBase() {

    @Test
    fun eval() {
        val arr = UltimateOscillator(7, 14, 28).eval(priceList)

        assertEqual(0.0, arr.first, "uo 0")
        assertEqual(0.0, arr[1], "uo 1")
        assertEqual(50.21, arr[28], "uo 28")
        assertEqual(63.82, arr[arr.size - 2], "uo last-1")
        assertEqual(56.02, arr.last, "uo last")
    }
}