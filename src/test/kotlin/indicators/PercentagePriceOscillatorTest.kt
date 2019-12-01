package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import org.junit.Test

class PercentagePriceOscillatorTest : TestBase() {

    @Test
    fun macd_temp() {
        // TODO temp to quick check PPO and PVO

        val arr = PercentagePriceOscillator(12, 26, 9).eval(priceList)
        assertEqual(-0.082, arr.last, "ppo last")
    }
}