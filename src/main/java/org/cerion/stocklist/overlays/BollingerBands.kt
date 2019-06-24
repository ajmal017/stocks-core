package org.cerion.stocklist.overlays

import org.cerion.stocklist.arrays.BandArray
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class BollingerBands() : OverlayBase<BandArray>(Overlay.BB, 20, 2.0) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String = "Bollinger Bands"

    override fun eval(arr: FloatArray): BandArray {
        return arr.bb(getInt(0), getFloat(1))
    }
}
