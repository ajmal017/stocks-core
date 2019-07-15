package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.Indicator

class WilliamsPercentR(period: Int = 14) : IndicatorBase(Indicator.WPR, period) {

    override val name: String = "Williams %R"

    override fun eval(list: PriceList): FloatArray {
        return williamsPercentR(list, getInt(0))
    }

    private fun williamsPercentR(list: PriceList, period: Int): FloatArray {
        val result = FloatArray(list.size)

        //%R = (Highest High - Close)/(Highest High - Lowest Low) * -100
        for (i in list.indices) {
            var h = list.high[i]
            var l = list.low[i]

            val count = ValueArray.maxPeriod(i, period)
            for (j in i - count + 1 until i) {
                h = Math.max(h, list.high[j])
                l = Math.min(l, list.low[j])
            }

            result[i] = (h - list.close[i]) / (h - l) * -100
        }

        return result
    }
}
