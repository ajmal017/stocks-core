package org.cerion.stocklist.indicators

import org.cerion.stocklist.TestBase
import org.junit.Test

class StochasticTest : TestBase() {

    @Test
    fun eval() {
        val stoch = Stochastic(14, 1, 1).eval(priceList)

        // Verified on stockcharts
        assertEqual(42.53, stoch.get(0), "position 0")
        assertEqual(2.47, stoch.get(1), "position 1")
        assertEqual(63.48, stoch.get(13), "position 13")
        assertEqual(25.02, stoch.get(14), "position 14")
        assertEqual(57.40, stoch.last(), "position last")
    }

}