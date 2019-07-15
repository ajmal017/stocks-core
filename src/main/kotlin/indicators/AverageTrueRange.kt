package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class AverageTrueRange(period: Int = 14) : IndicatorBase(Indicator.ATR, period) {

    override val name: String = "Average True Range"

    override fun eval(list: PriceList): FloatArray {
        return averageTrueRange(list, getInt(0))
    }

    private fun averageTrueRange(list: PriceList, period: Int): FloatArray {
        val result = FloatArray(list.size)

        //Current ATR = [(Prior ATR x 13) + Current TR] / 14
        result[0] = list.tr(0)
        for (i in 1 until list.size)
            result[i] = (result[i - 1] * (period - 1) + list.tr(i)) / period

        return result
    }
}
