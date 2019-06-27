package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class ChaikinOscillator(p1: Int, p2: Int) : IndicatorBase(Indicator.CO, p1, p2) {

    constructor() : this(3, 10)

    override val name: String = "Chaikin Oscillator"

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
