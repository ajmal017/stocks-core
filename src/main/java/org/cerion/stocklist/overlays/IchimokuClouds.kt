package org.cerion.stocklist.overlays

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.PairArray
import org.cerion.stocklist.functions.types.PriceOverlay

class IchimokuClouds(p1: Int, p2: Int, p3: Int) : PriceOverlayBase(PriceOverlay.CLOUD, p1, p2, p3) {

    constructor() : this(9, 26, 52)

    override fun eval(list: PriceList): PairArray {
        return ichimokuCloud(list, getInt(0), getInt(1), getInt(2))
    }

    override fun getName(): String = "Ichimoku Clouds"

    private fun ichimokuCloud(list: PriceList, conversion: Int, base: Int, span: Int): PairArray {
        val size = list.size
        val spanA = FloatArray(size)
        val spanB = FloatArray(size)

        val highs = list.high
        val lows = list.low

        for (i in span until size) {
            //Conversion Line
            var high = highs.max(i - conversion + 1, i)
            var low = lows.min(i - conversion + 1, i)
            val conversionLine = (high + low) / 2

            //Base line
            high = highs.max(i - base + 1, i)
            low = lows.min(i - base + 1, i)
            val baseLine = (high + low) / 2

            //Leading Span A
            spanA.mVal[i] = (conversionLine + baseLine) / 2

            //Leading Span B
            high = highs.max(i - span + 1, i)
            low = lows.min(i - span + 1, i)
            spanB.mVal[i] = (high + low) / 2
        }

        return PairArray(spanA, spanB)
    }
}
