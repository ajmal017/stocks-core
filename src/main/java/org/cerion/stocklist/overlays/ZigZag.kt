package org.cerion.stocklist.overlays

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.PriceOverlay

class ZigZag(percent: Double = 5.0) : PriceOverlayBase(PriceOverlay.ZIGZAG, percent) {

    override fun eval(list: PriceList): FloatArray {
        return zigzag(list, getFloat(0))
    }

    override val name: String = "ZigZag"

    private fun zzPercent(curr: Float, prev: Float): Float {
        return 100 * (curr - prev) / prev
    }

    private fun zigzag(list: PriceList, percent: Float): FloatArray {
        val size = list.size
        val result = FloatArray(list.size)

        // start=-1, down=0, up=1
        var direction = -1
        var currPos = 0

        for (i in list.indices) {
            val high = list.high(i)
            val low = list.low(i)
            val currHigh = list.high(currPos)
            val currLow = list.low(currPos)

            if (direction == -1) {
                if (zzPercent(high, currLow) > percent) {
                    direction = 1
                    currPos = i
                    result.mVal[0] = currLow
                } else if (zzPercent(low, currHigh) < -percent) {
                    direction = 0
                    currPos = i
                    result.mVal[0] = currHigh
                }
            } else if (direction == 0) {

                if (low < currLow)
                    currPos = i
                else if (zzPercent(high, currLow) > percent) {
                    result.mVal[currPos] = currLow
                    direction = 1
                    currPos = i
                }
            } else {
                if (high > currHigh)
                    currPos = i
                else if (zzPercent(low, currHigh) < -percent) {
                    result.mVal[currPos] = currHigh
                    direction = 0
                    currPos = i
                }
            }
        }

        // Add current position as the last point
        if (direction == 0)
            result.mVal[currPos] = list.low(currPos)
        else if (direction == 1)
            result.mVal[currPos] = list.high(currPos)

        // Add last point as the reverse direction
        val last = size - 1
        if (direction == 0)
            result.mVal[last] = list.high(last)
        else if (direction == 1)
            result.mVal[last] = list.low(last)

        // Fix by adding straight lines to connect points
        var start = 0
        var end: Int
        for (i in 1 until size) {
            if (result.get(i) != 0f) {
                end = i
                val a = result.get(start)
                val b = result.get(end)
                val inc = (b - a) / (end - start)

                for (j in start + 1 until end) {
                    result.mVal[j] = result.mVal[j - 1] + inc
                }

                start = end
            }
        }

        return result
    }
}
