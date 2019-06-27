package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class SimpleMovingAverage(period: Int = 50) : OverlayBase<FloatArray>(Overlay.SMA, period) {

    override val name: String = "Simple Moving Average"

    override fun eval(arr: FloatArray): FloatArray {
        return arr.sma(getInt(0))
    }
}
