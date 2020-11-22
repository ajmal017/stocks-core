package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.TestBase
import kotlin.test.Test

class IchimokuCloudsTest : TestBase() {

    @Test
    fun eval() = runPriceTest {
        val size = it.size
        var arr = IchimokuClouds(9, 26, 52).eval(it)

        assertEqual(2046.11, arr.pos(size- 1), "ichimokuCloud SpanA last")
        assertEqual(2054.87, arr.neg(size - 1), "ichimokuCloud SpanB last")

        arr = IchimokuClouds(5, 15, 30).eval(it)
        assertEqual(2050.0, arr.pos(size - 1), "ichimokuCloud SpanA last with different parameters")
        assertEqual(2048.77, arr.neg(size - 1), "ichimokuCloud SpanB last with different parameters")
    }
}