package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class ChaikinOscillator() : IndicatorBase(Indicator.CO, 3, 10) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "Chaikin Oscillator"
    }

    override fun eval(list: PriceList): FloatArray {
        return chaikinOscillator(list, getInt(0), getInt(1))
    }

    private fun chaikinOscillator(list: PriceList, p1: Int, p2: Int): FloatArray {
        val result = FloatArray(list.size)

        val adl = AccumulationDistributionLine().eval(list)
        val ema1 = adl.ema(p1)
        val ema2 = adl.ema(p2)

        for (i in list.indices)
            result.mVal[i] = ema1.get(i) - ema2.get(i)

        return result
    }

}
