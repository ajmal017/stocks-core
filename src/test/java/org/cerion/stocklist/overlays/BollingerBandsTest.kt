package org.cerion.stocklist.overlays

import org.cerion.stocklist.functions.FunctionTestBase
import org.junit.Test

class BollingerBandsTest : FunctionTestBase() {

    @Test
    fun bollingerBands_defaults() {
        val bands = BollingerBands()
        val arr=  bands.eval(mPriceList)

        // TODO need to verify online
        assertEqual(4.32, arr.bandwidth(arr.size - 1), "bandwidth")
        assertEqual(2006.05, arr.lower(arr.size - 1), "lower")
        assertEqual(2050.38, arr.mid(arr.size - 1), "mid")
        assertEqual(0.43, arr.percent(arr.size - 1), "percent")
        assertEqual(2094.71, arr.upper(arr.size - 1), "upper")
    }
}