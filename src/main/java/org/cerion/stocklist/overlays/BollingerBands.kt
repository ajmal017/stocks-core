package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.BandArray
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class BollingerBands(period: Int, stddev: Double) : OverlayBase<BandArray>(Overlay.BB, period, stddev) {

    constructor() : this(20, 2.0)

    override fun getName(): String = "Bollinger Bands"

    override fun eval(arr: FloatArray): BandArray {
        return arr.bb(getInt(0), getFloat(1))
    }
}
