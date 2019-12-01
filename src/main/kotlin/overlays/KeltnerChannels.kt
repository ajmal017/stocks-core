package org.cerion.stocks.core.overlays

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.BandArray
import org.cerion.stocks.core.functions.types.PriceOverlay
import org.cerion.stocks.core.indicators.AverageTrueRange
import org.cerion.stocks.core.arrays.FloatArray

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

        val upper = FloatArray(list.size)
        val lower = FloatArray(list.size)
        val atr = AverageTrueRange(atrPeriod).eval(list)

        for (i in 1 until list.size) {
            upper[i] = ema[i] + multiplier * atr[i]
            lower[i] = ema[i] - multiplier * atr[i]
        }

        return BandArray(list.close, upper, lower)
    }
}