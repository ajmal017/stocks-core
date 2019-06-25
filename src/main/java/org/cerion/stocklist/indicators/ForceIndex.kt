package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Indicator

class ForceIndex() : IndicatorBase(Indicator.FORCE_INDEX, 13) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "Force Index"
    }

    override fun eval(list: PriceList): FloatArray {
        return forceIndex(list, getInt(0))
    }

    private fun forceIndex(list: PriceList, period: Int): FloatArray {
        val close = list.mClose
        val size = list.size
        val result = FloatArray(size)

        val mult = 2.0f / (1f + period)

        for (i in 1 until size) {
            //Price p = get(i);
            //Price prev = get(i-1);

            val fi = (close.get(i) - close.get(i - 1)) * list.mVolume.get(i)
            result.mVal[i] = (fi - result.get(i - 1)) * mult + result.get(i - 1)
            //System.out.println(p.date + "\t" + p.fi);
        }

        return result
    }
}
