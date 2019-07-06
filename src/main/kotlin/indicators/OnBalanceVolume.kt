package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class OnBalanceVolume : IndicatorBase(Indicator.OBV) {

    override val name: String = "On Balance Volume"

    override fun eval(list: PriceList): FloatArray {
        return onBalanceVolume(list)
    }

    private fun onBalanceVolume(list: PriceList): FloatArray {
        val close = list.mClose
        val volume = list.mVolume
        val result = FloatArray(list.size)

        result[0] = 0f
        for (i in 1 until list.size) {
            if (close.get(i) > close.get(i - 1))
                result[i] = result.get(i - 1) + volume.get(i)
            else if (close.get(i) < close.get(i - 1))
                result[i] = result.get(i - 1) - volume.get(i)
            else
                result[i] = result.get(i - 1)
        }

        return result
    }
}
