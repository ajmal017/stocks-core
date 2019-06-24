package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class Line() : OverlayBase<FloatArray>(Overlay.LINE, 1.0) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String {
        return "Line"
    }

    override fun eval(arr: FloatArray): FloatArray {
        return arr.line(getFloat(0))
    }
}
