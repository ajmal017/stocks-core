package org.cerion.stocklist.overlays


import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.types.Overlay

class LinearRegressionLine : OverlayBase<FloatArray>(Overlay.LINREG) {

    override val name: String = "Linear Regression Line"

    override fun eval(arr: FloatArray): FloatArray {

        val pos = arr.size - 1
        val result = FloatArray(arr.size)

        val ab = arr.getLinearRegressionEquation(0, pos)
        val slope = ab[1]
        result[0] = ab[0]

        for (i in 1 until arr.size) {
            result[i] = result[i - 1] + slope
        }

        return result
    }
}
