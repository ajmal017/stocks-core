package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.junit.Test

class PriceChannelsTest : TestBase() {

    @Test
    fun priceChannels_defaults() {
        val arr = PriceChannels().eval(priceList)

        assertEqual(priceList.high(0).toDouble(), arr.upper(0), "priceChannels 0")
        assertEqual(priceList.low(0).toDouble(), arr.lower(0), "priceChannels 0")
        assertEqual(1478.0, arr.upper(1), "priceChannels 1 upper")
        assertEqual(1438.36, arr.lower(1), "priceChannels 1 lower")
        assertEqual(1478.0, arr.upper(18), "priceChannels 18")
        assertEqual(1478.0, arr.upper(19), "priceChannels 19")
        assertEqual(1350.14, arr.lower(20), "priceChannels 20")

        // TODO add assert function that takes BandArray and position with 5 values
        // Last
        val p = size - 1
        assertEqual(2104.27, arr.upper(p), "priceChannels upper last")
        assertEqual(1993.26, arr.lower(p), "priceChannels lower last")
        assertEqual(2048.77, arr.mid(p), "mid last")
        assertEqual(5.42, arr.bandwidth(p), "bandwidth last")
        assertEqual(0.4565, arr.percent(p), "percent last")
    }

    @Test
    fun priceChannels_100() {
        val arr = PriceChannels(100).eval(priceList)
        assertEqual(2116.48, arr.upper(size - 1), "priceChannels upper last with different parameters")
        assertEqual(1867.01, arr.lower(size - 1), "priceChannels lower last with different parameters")
    }

}