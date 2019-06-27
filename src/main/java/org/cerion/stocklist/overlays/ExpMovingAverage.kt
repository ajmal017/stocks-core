package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class ExpMovingAverage(period: Int = 20) : OverlayBase<FloatArray>(Overlay.EMA, period) {

    override val name: String = "Exp. Moving Average"

    override fun eval(arr: FloatArray): FloatArray {
        return arr.ema(getInt(0))
    }
}
