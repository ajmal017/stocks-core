package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class PriceChannelsTest : TestBase() {

    @Test
    fun priceChannels_defaults() = runPriceTest {
        val arr = PriceChannels().eval(it)

        assertEqual(it.high[0], arr.upper(0), "priceChannels 0")
        assertEqual(it.low[0], arr.lower(0), "priceChannels 0")
        assertEqual(1478.0, arr.upper(1), "priceChannels 1 upper")
        assertEqual(1438.36, arr.lower(1), "priceChannels 1 lower")
        assertEqual(1478.0, arr.upper(18), "priceChannels 18")
        assertEqual(1478.0, arr.upper(19), "priceChannels 19")
        assertEqual(1350.14, arr.lower(20), "priceChannels 20")

        // TODO add assert function that takes BandArray and position with 5 values
        // Last
        val p = it.size - 1
        assertEqual(2104.27, arr.upper(p), "priceChannels upper last")
        assertEqual(1993.26, arr.lower(p), "priceChannels lower last")
        assertEqual(2048.77, arr.mid(p), "mid last")
        assertEqual(5.42, arr.bandwidth(p), "bandwidth last")
        assertEqual(0.46, arr.percent(p), "percent last")
    }

    @Test
    fun priceChannels_100() = runPriceTest {
        val arr = PriceChannels(100).eval(it)
        assertEqual(2116.48, arr.upper(it.size - 1), "priceChannels upper last with different parameters")
        assertEqual(1867.01, arr.lower(it.size - 1), "priceChannels lower last with different parameters")
    }
}