package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.Indicator

class StochasticRSI(period: Int = 14) : IndicatorBase(Indicator.STOCHRSI, period) {

    override val name: String = "Stochastic RSI"

    override fun eval(list: PriceList): FloatArray {
        return stochasticRSI(list, getInt(0))
    }

    private fun stochasticRSI(list: PriceList, period: Int): FloatArray {
        val result = FloatArray(list.size)
        val rsi_arr = RSI(period).eval(list)

        for (i in list.indices) {
            var high = rsi_arr.get(i)
            var low = rsi_arr.get(i)

            val count = ValueArray.maxPeriod(i, period)
            for (j in i - count + 1 until i) {
                val rsi = rsi_arr.get(j)
                if (rsi > high)
                    high = rsi
                if (rsi < low)
                    low = rsi
            }

            //StochRSI = (RSI - Lowest Low RSI) / (Highest High RSI - Lowest Low RSI)
            if (high == low)
                result.mVal[i] = 1f
            else
                result.mVal[i] = (rsi_arr.get(i) - low) / (high - low)
        }

        return result
    }

}
