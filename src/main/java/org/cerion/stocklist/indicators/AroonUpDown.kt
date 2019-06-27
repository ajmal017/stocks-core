package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.PairArray
import org.cerion.stocklist.functions.types.Indicator

class AroonUpDown(period: Int = 25) : IndicatorBase(Indicator.AROON, period) {

    override val name: String = "Aroon Up/Down"

    override fun eval(list: PriceList): PairArray {
        return aroon(list, getInt(0))
    }

    private fun aroon(list: PriceList, period: Int): PairArray {
        val size = list.size
        val up = FloatArray(size)
        val down = FloatArray(size)
        //Aroon Up = 100 x (25 - Days Since 25-day High)/25
        //Aroon Down = 100 x (25 - Days Since 25-day Low)/25
        //Aroon Oscillator = Aroon-Up  -  Aroon-Down

        for (i in period - 1 until size) {
            val high = i - list.mClose.maxPos(i - period + 1, i) + 1
            val low = i - list.mClose.minPos(i - period + 1, i) + 1

            up.mVal[i] = (100 * (period - high) / period).toFloat()
            down.mVal[i] = (100 * (period - low) / period).toFloat()
        }

        return PairArray(up, down)
    }
}
