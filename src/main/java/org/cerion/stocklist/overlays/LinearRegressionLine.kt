package org.cerion.stocklist.overlays


import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class LinearRegressionLine() : OverlayBase<FloatArray>(Overlay.LINREG) {

    constructor(vararg params: Number) : this() {
        setParams(*params)
    }

    override fun getName(): String = "Linear Regression Line"

    override fun eval(arr: FloatArray): FloatArray {
        return arr.linearRegressionLine()
    }
}
