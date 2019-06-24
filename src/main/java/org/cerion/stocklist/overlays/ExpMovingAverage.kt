package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class ExpMovingAverage() : OverlayBase<FloatArray>(Overlay.EMA, 20) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String = "Exp. Moving Average"

    override fun eval(arr: FloatArray): FloatArray {
        return arr.ema(getInt(0))
    }
}
