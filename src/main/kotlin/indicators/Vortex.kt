package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.PairArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.Indicator

class Vortex(period: Int = 14) : IndicatorBase(Indicator.VORTEX, period) {

    override val name: String = "Vortex"

    override fun eval(list: PriceList): PairArray {
        return vortex(list, getInt(0))
    }

    private fun vortex(list: PriceList, period: Int): PairArray {
        val size = list.size
        val posVI = FloatArray(size)
        val negVI = FloatArray(size)

        val vm = Array(size) { kotlin.FloatArray(2) } // +VM/-VM

        for (i in 1 until size) {
            vm[i][0] = Math.abs(list.high[i] - list.low[i - 1])
            vm[i][1] = Math.abs(list.low[i] - list.high[i - 1])
        }

        // Start at 1 since that is the average value
        posVI[0] = 1f
        negVI[0] = 1f

        for (i in 1 until size) {
            val count = ValueArray.maxPeriod(i, period)
            var vip = 0f
            var vin = 0f
            var tr = 0f
            for (j in i - count + 1..i) {
                vip += vm[j][0]
                vin += vm[j][1]
                tr += list.tr(j)
            }

            posVI[i] = vip / tr
            negVI[i] = vin / tr
        }

        return PairArray(posVI, negVI)
    }

}
