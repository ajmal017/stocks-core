package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import org.junit.Test

class KeltnerChannelsTest : TestBase() {

    @Test
    fun keltnerChannels_defaults() {
        val arr = KeltnerChannels().eval(priceList)

        val last = arr.size - 1
        // TODO verify values online, just doing these pre-refactor
        assertEqual(4.62, arr.bandwidth(last), "bandwidth")
        assertEqual(2007.02, arr.lower(last), "lower")
        assertEqual(2101.92, arr.upper(last), "upper")
        assertEqual(0.39, arr.percent(last), "percent")
        assertEqual(2054.47, arr.mid(last), "mid")
    }
}