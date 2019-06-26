package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.Indicator

class CommodityChannelIndex(period: Int = 20) : IndicatorBase(Indicator.CCI, period) {

    override fun getName(): String {
        return "Commodity Channel Index"
    }

    override fun eval(list: PriceList): FloatArray {
        return commodityChannelIndex(list, getInt(0))
    }

    private fun commodityChannelIndex(list: PriceList, period: Int): FloatArray {
        val size = list.size
        val result = FloatArray(size)

        val tp = FloatArray(size)
        for (i in 0 until size)
            tp.mVal[i] = list.tp(i)

        val smaArr = tp.sma(period)

        for (i in 1 until size) {
            val sma = smaArr.get(i)
            val count = ValueArray.maxPeriod(i, period)

            //Mean deviation is different than standard deviation
            var dev = 0f
            for (j in i - count + 1..i)
                dev += Math.abs(list[j].tp() - sma)
            dev /= count

            //CCI = (Typical Price  -  20-period SimpleMovingAverage of TP) / (.015 x Mean Deviation)
            result.mVal[i] = (list.tp(i) - sma) / (0.015f * dev)
        }

        return result
    }
}
