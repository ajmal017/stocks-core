package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.MACDArray
import org.cerion.stocklist.functions.types.Indicator

class MACD() : IndicatorBase(Indicator.MACD, 12, 26, 9) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "MACD"
    }

    override fun eval(list: PriceList): MACDArray {
        return macd(list, getInt(0), getInt(1), getInt(2))
    }

    private fun macd(list: PriceList, p1: Int, p2: Int, signal: Int): MACDArray {
        val result = MACDArray(list.size)
        val ema1 = list.mClose.ema(p1)
        val ema2 = list.mClose.ema(p2)

        for (i in list.indices)
            result.mVal[i] = ema1.get(i) - ema2.get(i)

        result.setSignal(signal)
        return result
    }
}
