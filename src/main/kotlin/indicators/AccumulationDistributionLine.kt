package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.FloatArray
import org.cerion.stocks.core.functions.types.Indicator

class AccumulationDistributionLine : IndicatorBase(Indicator.ADL) {

    override val name: String = "Accumulation Distribution Line"

    override fun eval(list: PriceList): FloatArray {
        return accumulationDistributionLine(list)
    }

    private fun accumulationDistributionLine(list: PriceList): FloatArray {
        val result = FloatArray(list.size)

        result[0] = 0f
        for (i in 1 until list.size) {
            //ADL = Previous ADL + Current Period's Money Flow Volume
            result[i] = result[i - 1] + list.mfv(i)
        }

        return result
    }
}
