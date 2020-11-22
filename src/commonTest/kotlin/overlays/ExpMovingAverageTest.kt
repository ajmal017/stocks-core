package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpMovingAverageTest : TestBase() {

    @Test
    fun ema_tests() = runPriceTest {
        // Not verified online although many other indicators use this so its tested indirectly
        var ema = ExpMovingAverage().eval(it)
        assertEquals(it.close.first, ema.first)
        assertEqual(2054.47, ema.last)

        ema = ExpMovingAverage(123).eval(it)
        assertEquals(it.close.first, ema.first)
        assertEqual(2047.90, ema.last)
    }
}