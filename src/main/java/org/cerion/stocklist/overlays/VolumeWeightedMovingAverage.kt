package org.cerion.stocklist.overlays


import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.PriceOverlay

class VolumeWeightedMovingAverage(period: Int = 20) : PriceOverlayBase(PriceOverlay.VWMA, period) {

    override fun getName(): String = "Volume Weighted Moving Average"

    override fun eval(list: PriceList): FloatArray {
        val size = list.size
        val period = getInt(0)
        val result = FloatArray(size)

        for (i in 0 until size) {
            val count = ValueArray.maxPeriod(i, period)

            var total = 0f
            var volume = 0f
            for (j in i - count + 1..i) {
                volume += list.volume(j)
                total += list.close(j) * list.volume(j)
            }

            result.mVal[i] = total / volume

        }

        return result
    }
}
