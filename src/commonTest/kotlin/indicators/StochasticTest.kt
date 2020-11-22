package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class StochasticTest : TestBase() {

    @Test
    fun eval() = runPriceTest {
        val stoch = Stochastic(14, 1, 1).eval(it)

        // Verified on stockcharts
        assertEqual(42.53, stoch.first, "position 0")
        assertEqual(2.47, stoch[1], "position 1")
        assertEqual(63.48, stoch[13], "position 13")
        assertEqual(25.02, stoch[14], "position 14")
        assertEqual(57.40, stoch.last, "position last")
    }

}