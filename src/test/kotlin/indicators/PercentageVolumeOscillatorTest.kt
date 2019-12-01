package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.TestBase
import org.junit.Test

class PercentageVolumeOscillatorTest : TestBase() {

    @Test
    fun macd_temp() {
        // TODO temp to quick check PPO and PVO

        val arr = PercentageVolumeOscillator(12, 26, 9).eval(priceList)
        assertEqual(-10.86, arr.last, "pvo last")
    }
}