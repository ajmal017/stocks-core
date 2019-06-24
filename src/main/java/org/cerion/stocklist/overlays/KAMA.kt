package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class KAMA() : OverlayBase<FloatArray>(Overlay.KAMA, 10, 2, 30) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String = "Adaptive Moving Average"

    override fun eval(arr: FloatArray): FloatArray {
        return arr.kama(getInt(0), getInt(1), getInt(2))
    }
}
