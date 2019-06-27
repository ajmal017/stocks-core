package org.cerion.stocklist.overlays

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.BandArray
import org.cerion.stocklist.functions.types.PriceOverlay
import org.cerion.stocklist.indicators.AverageTrueRange

class KeltnerChannels(period: Int, multiplier: Double, atr: Int) : PriceOverlayBase(PriceOverlay.KC, period, multiplier, atr) {

    constructor() : this(20, 2.0, 10)

    override val name: String = "Keltner Channels"

    override fun eval(list: PriceList): BandArray {
        val emaPeriod = getInt(0)
        val multiplier = getFloat(1)
        val atrPeriod = getInt(2)

        val ema = ExpMovingAverage(emaPeriod).eval(list)
        //Middle Line: 20-day exponential moving average
        //Upper Channel Line: 20-day ExpMovingAverage + (2 x ATR(10))
        //Lower Channel Line: 20-day ExpMovingAverage - (2 x ATR(10))
        return BandArray(list.mClose, multiplier, ema, AverageTrueRange(atrPeriod).eval(list))
    }
}
