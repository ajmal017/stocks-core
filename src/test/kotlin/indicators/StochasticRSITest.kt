package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import org.junit.Test

class StochasticRSITest : TestBase() {

    @Test
    fun stochasticRSI_defaults() {
        val arr = StochasticRSI().eval(priceList)

        // TODO verify value online, just doing these pre-refactor
        assertEqual(0.57, arr.last, "last")
    }
}