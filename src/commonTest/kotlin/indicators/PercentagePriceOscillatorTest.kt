package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class PercentagePriceOscillatorTest : TestBase() {

    @Test
    fun macd_temp() = runPriceTest {
        // TODO temp to quick check PPO and PVO

        val arr = PercentagePriceOscillator(12, 26, 9).eval(it)
        assertEqual(-0.08, arr.last, "ppo last")
    }
}