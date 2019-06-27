package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class AccumulationDistributionLine : IndicatorBase(Indicator.ADL) {

    override val name: String = "Accumulation Distribution Line"

    override fun eval(list: PriceList): FloatArray {
        return accumulationDistributionLine(list)
    }

    private fun accumulationDistributionLine(list: PriceList): FloatArray {
        val result = FloatArray(list.size)

        result.mVal[0] = 0f
        for (i in 1 until list.size) {
            //ADL = Previous ADL + Current Period's Money Flow Volume
            result.mVal[i] = result.mVal[i - 1] + list.mfv(i)
        }

        return result
    }
}
