package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class KAMA(er: Int, fast: Int, slow: Int) : OverlayBase<FloatArray>(Overlay.KAMA, er, fast, slow) {

    constructor() : this(10, 2, 30)

    override fun getName(): String = "Adaptive Moving Average"

    override fun eval(arr: FloatArray): FloatArray {
        return arr.kama(getInt(0), getInt(1), getInt(2))
    }
}
