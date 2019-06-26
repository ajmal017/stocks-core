package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.Indicator

class AverageDirectionalIndex(period: Int = 14) : IndicatorBase(Indicator.ADX, period) {

    override fun getName(): String {
        return "Average Directional Index"
    }

    override fun eval(list: PriceList): FloatArray {
        return averageDirectionalIndex(list, getInt(0))
    }

    private fun averageDirectionalIndex(list: PriceList, period: Int): FloatArray {
        val size = list.size
        val result = FloatArray(size)
        val di = DirectionalIndex(period).eval(list)

        for (i in 1 until size) {
            val count = ValueArray.maxPeriod(i, period)
            //Directional Movement Index (DX) equals the absolute value of +DI minus -DI divided by the sum of +DI and -DI.
            val diff = di.getPos(i) - di.getNeg(i)
            val sum = di.getPos(i) + di.getNeg(i)

            val dx = 100 * (Math.abs(diff) / sum)
            result.mVal[i] = (result.get(i - 1) * (count - 1) + dx) / count
        }

        return result
    }

}
