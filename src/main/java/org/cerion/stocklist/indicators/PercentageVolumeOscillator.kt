package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.MACDArray
import org.cerion.stocklist.functions.types.Indicator

class PercentageVolumeOscillator(p1: Int, p2: Int, signal: Int) : IndicatorBase(Indicator.PVO, p1, p2, signal) {

    constructor() : this(12, 26, 9)

    override fun getName(): String {
        return "Percentage Volume Oscillator"
    }

    override fun eval(list: PriceList): MACDArray {
        return PercentagePriceOscillator.getPercentMACD(list.mVolume, getInt(0), getInt(1), getInt(2))
    }
}
