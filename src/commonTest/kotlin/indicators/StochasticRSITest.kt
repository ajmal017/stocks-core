package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class StochasticRSITest : TestBase() {

    @Test
    fun stochasticRSI_defaults() = runPriceTest {
        val arr = StochasticRSI().eval(it)

        // TODO verify value online, just doing these pre-refactor
        assertEqual(0.57, arr.last, "last")
    }
}