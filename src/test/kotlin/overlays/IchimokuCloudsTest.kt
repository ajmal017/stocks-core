package org.cerion.stocklist.overlays

import org.cerion.stocklist.TestBase
import org.junit.Test

class IchimokuCloudsTest : TestBase() {

    @Test
    fun eval() {
        var arr = IchimokuClouds(9, 26, 52).eval(priceList)

        assertEqual(2046.11, arr.pos( size- 1), "ichimokuCloud SpanA last")
        assertEqual(2054.87, arr.neg(size - 1), "ichimokuCloud SpanB last")

        arr = IchimokuClouds(5, 15, 30).eval(priceList)
        assertEqual(2050.0, arr.pos(size - 1), "ichimokuCloud SpanA last with different parameters")
        assertEqual(2048.77, arr.neg(size - 1), "ichimokuCloud SpanB last with different parameters")
    }
}