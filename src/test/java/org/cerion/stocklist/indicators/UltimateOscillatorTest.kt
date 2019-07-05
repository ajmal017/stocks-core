package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test


class UltimateOscillatorTest : TestBase() {

    @Test
    fun eval() {
        val arr = UltimateOscillator(7, 14, 28).eval(priceList)

        assertEqual(0.0, arr.first(), "uo 0")
        assertEqual(0.0, arr.get(1), "uo 1")
        assertEqual(50.21, arr.get(28), "uo 28")
        assertEqual(63.82, arr.get(arr.size - 2), "uo last-1")
        assertEqual(56.02, arr.last(), "uo last")
    }
}