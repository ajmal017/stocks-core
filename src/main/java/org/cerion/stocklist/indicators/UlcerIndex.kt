package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.Indicator

class UlcerIndex(period: Int = 14) : IndicatorBase(Indicator.ULCER_INDEX, period) {

    override val name: String = "Ulcer Index"

    override fun eval(list: PriceList): FloatArray {
        return eval(list.close)
    }

    fun eval(arr: FloatArray): FloatArray {
        val period = getInt(0)
        val size = arr.size
        val result = FloatArray(size)

        //Percent-Drawdown = ((Close - 14-period Max Close)/14-period Max Close) x 100
        //Squared Average = (14-perod Sum of Percent-Drawdown Squared)/14
        //Ulcer Index = Square Root of Squared Average

        //Set Percent Drawdown
        val percentD = FloatArray(size)
        for (i in 0 until size) {
            var max = 0f //Max close
            val count = ValueArray.maxPeriod(i, period)
            for (j in i - count + 1..i)
                max = Math.max(max, arr.get(j))

            percentD[i] = (arr.get(i) - max) / max * 100
        }

        for (i in 0 until size) {
            var avg = 0f
            val count = ValueArray.maxPeriod(i, period)
            for (j in i - count + 1..i)
                avg += percentD[j] * percentD[j] //Sum of squared

            avg /= period.toFloat()
            result.mVal[i] = Math.sqrt(avg.toDouble()).toFloat()
        }

        return result
    }
}
