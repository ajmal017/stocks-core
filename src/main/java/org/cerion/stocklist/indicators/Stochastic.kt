package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.Indicator

class Stochastic() : IndicatorBase(Indicator.STOCH, 14, 3, 3) {

    // TODO allow 1-3 parameters
    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "Stochastic Oscillator"
    }

    override fun eval(list: PriceList): FloatArray {
        return stochastic(list, getInt(0), getInt(1), getInt(2))
    }

    private fun stochastic(list: PriceList, K: Int): FloatArray {
        val result = FloatArray(list.size)
        val highs = list.high
        val lows = list.low

        //K = period
        for (i in list.indices) {
            val period = ValueArray.maxPeriod(i, K)

            val high = highs.max(i - period + 1, i)
            val low = lows.min(i - period + 1, i)

            //K = (Current Close - Lowest Low)/(Highest High - Lowest Low) * 100
            result.mVal[i] = (list.close(i) - low) / (high - low) * 100
        }

        return result
    }

    private fun stochastic(list: PriceList, K: Int, D: Int): FloatArray {
        val result = stochastic(list, K)
        return result.sma(D)
    }

    private fun stochastic(list: PriceList, K: Int, fastD: Int, slowD: Int): FloatArray {
        val result = stochastic(list, K, fastD)
        return result.sma(slowD)
    }

}
