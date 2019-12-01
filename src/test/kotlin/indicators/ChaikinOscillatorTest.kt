package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import org.junit.Test

class ChaikinOscillatorTest : TestBase() {

    @Test
    fun chaikinOscillator_test_defaults() {
        val arr = ChaikinOscillator().eval(priceList)

        // TODO verify values online, just doing these pre-refactor
        assertEqual(0.0, arr.first, "first")
        assertEqual(-298935.09, arr[1], "position 1")
        assertEqual(-265271.78, arr[2], "position 2")
        assertEqual(550272.0, arr.last, "last")
    }
}