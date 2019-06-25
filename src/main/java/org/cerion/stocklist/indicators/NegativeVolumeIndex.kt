package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class NegativeVolumeIndex : IndicatorBase(Indicator.NVI) {

    override fun getName(): String {
        return "Negative Volume Index"
    }

    override fun eval(list: PriceList): FloatArray {
        return negativeVolumeIndex(list)
    }

    private fun negativeVolumeIndex(list: PriceList): FloatArray {
        val result = FloatArray(list.size)

        result.mVal[0] = 1000f
        for (i in 1 until list.size) {
            if (list.volume(i) < list.volume(i - 1))
                result.mVal[i] = result.mVal[i - 1] + list.roc(i, 1)
            else
                result.mVal[i] = result.mVal[i - 1]

        }

        return result
    }
}
