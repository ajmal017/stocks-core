package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.MACDArray
import org.cerion.stocklist.functions.types.Indicator

class PercentageVolumeOscillator() : IndicatorBase(Indicator.PVO, 12, 26, 9) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "Percentage Volume Oscillator"
    }

    override fun eval(list: PriceList): MACDArray {
        return PercentagePriceOscillator.getPercentMACD(list.mVolume, getInt(0), getInt(1), getInt(2))
    }
}
