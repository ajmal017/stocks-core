package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.MACDArray
import org.cerion.stocklist.functions.types.Indicator

class PercentagePriceOscillator(p1: Int, p2: Int, signal: Int) : IndicatorBase(Indicator.PPO, p1, p2, signal) {

    constructor() : this(12, 26, 9)

    override val name: String = "Percentage Price Oscillator"

    override fun eval(list: PriceList): MACDArray {
        //Percentage version of MACD
        return getPercentMACD(list.mClose, getInt(0), getInt(1), getInt(2))
    }

    companion object {

        // Shared with PVO
        fun getPercentMACD(arr: FloatArray, p1: Int, p2: Int, signal: Int): MACDArray {
            val result = MACDArray(arr.size())
            val ema1 = arr.ema(p1)
            val ema2 = arr.ema(p2)

            for (i in 0 until arr.size())
                result.mVal[i] = 100 * (ema1.get(i) - ema2.get(i)) / ema2.get(i)

            result.setSignal(signal)
            return result
        }
    }

}
