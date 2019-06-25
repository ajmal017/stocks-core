package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class PriceMomentumOscillator() : IndicatorBase(Indicator.PMO, 35, 20) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "Price Momentum Oscillator"
    }

    override fun eval(list: PriceList): FloatArray {
        return priceMomentumOscillator(list, getInt(0), getInt(1))
    }

    private fun priceMomentumOscillator(list: PriceList, p1: Int, p2: Int): FloatArray {
        val result = FloatArray(list.size)

        val m1 = 2.0f / p1
        val m2 = 2.0f / p2
        var ema = 0f

        for (i in 1 until list.size) {
            val roc = list.roc(i, 1)

            ema = roc * m1 + ema * (1 - m1)

            val e = ema * 10
            result.mVal[i] = (e - result.get(i - 1)) * m2 + result.get(i - 1)
        }

        return result
    }

}
