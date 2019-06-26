package org.cerion.stocklist.indicators


import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator
import org.cerion.stocklist.model.Interval

class SharpeRatio(period: Int, riskFreeRate: Double) : IndicatorBase(Indicator.SHARPE, period, riskFreeRate) {

    // Default 0.75 estimate of 3-month US treasury
    constructor() : this(10, 0.75)

    override fun eval(list: PriceList): FloatArray {
        val years = getInt(0)
        val multiplier: Int
        when (list.interval) {
            Interval.DAILY -> multiplier = 252
            Interval.WEEKLY -> multiplier = 52
            Interval.MONTHLY -> multiplier = 12
            Interval.QUARTERLY -> multiplier = 4
            Interval.YEARLY -> multiplier = 1
            else -> throw RuntimeException("unexpected interval " + list.interval)
        }

        val change = list.close.percentChange
        val riskFree = getFloat(1) / 100 / multiplier

        for (i in 1 until change.size()) {
            change.mVal[i] = change.mVal[i] - riskFree
        }

        val avg = change.sma(years * multiplier)
        val std = change.std(years * multiplier)

        val result = FloatArray(list.size)
        for (i in list.indices) {
            if (i >= multiplier) {
                result.mVal[i] = avg.get(i) / std.get(i)
                result.mVal[i] *= Math.sqrt(multiplier.toDouble()).toFloat()
            } else
                result.mVal[i] = java.lang.Float.NaN
        }

        return result
    }

    override fun getName(): String {
        return "Sharpe Ratio"
    }
}
