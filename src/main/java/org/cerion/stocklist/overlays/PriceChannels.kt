package org.cerion.stocklist.overlays

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.BandArray
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.PriceOverlay

class PriceChannels(period: Int = 20) : PriceOverlayBase(PriceOverlay.CHAN, period) {

    override fun eval(list: PriceList): BandArray {
        return priceChannels(list, getInt(0))
    }

    override val name: String = "Price Channels"

    private fun priceChannels(list: PriceList, period: Int): BandArray {
        val size = list.size
        val upper = FloatArray(size)
        val lower = FloatArray(size)

        upper.mVal[0] = list.high.get(0)
        lower.mVal[0] = list.low.get(0)

        for (i in 1 until size) {
            val p = ValueArray.maxPeriod(i, period)
            val start = Math.max(i - p, 0)
            upper.mVal[i] = list.high.max(start, i - 1)
            lower.mVal[i] = list.low.min(start, i - 1)
        }

        return BandArray(upper, lower)
    }
}
