package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class Line(slope: Double = 1.0) : OverlayBase<FloatArray>(Overlay.LINE, slope) {

    override val name: String = "Line"

    override fun eval(arr: FloatArray): FloatArray {
        return arr.line(getFloat(0))
    }
}
