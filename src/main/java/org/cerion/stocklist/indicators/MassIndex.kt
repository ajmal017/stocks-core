package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class MassIndex() : IndicatorBase(Indicator.MASS_INDEX, 25) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "Mass Index"
    }

    override fun eval(list: PriceList): FloatArray {
        return massIndex(list, getInt(0))
    }

    private fun massIndex(list: PriceList, period: Int): FloatArray {
        val size = list.size
        val result = FloatArray(size)

        //Single ExpMovingAverage = 9-period exponential moving average (ExpMovingAverage) of the high-low differential
        //Double ExpMovingAverage = 9-period ExpMovingAverage of the 9-period ExpMovingAverage of the high-low differential
        //ExpMovingAverage Ratio = Single ExpMovingAverage divided by Double ExpMovingAverage
        //Mass Index = 25-period sum of the ExpMovingAverage Ratio

        val highLowDiff = FloatArray(size)
        for (i in 0 until size) {
            highLowDiff.mVal[i] = list.high(i) - list.low(i)
        }

        val ema = highLowDiff.ema(9)
        val ema2 = ema.ema(9)
        val emaRatio = FloatArray(size)

        //X period sum
        for (i in 0 until size) {
            emaRatio.mVal[i] = ema.get(i) / ema2.get(i)

            //int max = ValueArray.maxPeriod(i, period);
            if (i >= period - 1) {
                result.mVal[i] = emaRatio.sum(i - period + 1, i)
            } else if (i == 0) {
                result.mVal[i] = period.toFloat()
            } else {
                // Normalize, average ema ratio is 1
                // X period sum of ratio is average of the period size
                // Anything value before the period size will be normalized so the average stays the same
                val mult = period / (1.0f + i)
                result.mVal[i] = mult * emaRatio.sum(0, i)
            }
        }

        return result
    }
}
