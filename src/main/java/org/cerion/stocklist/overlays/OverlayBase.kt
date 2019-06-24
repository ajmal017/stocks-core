package org.cerion.stocklist.overlays


import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.ISimpleOverlay
import org.cerion.stocklist.functions.types.IFunctionEnum

abstract class OverlayBase<T : ValueArray> internal constructor(id: IFunctionEnum, vararg params: Number) : PriceOverlayBase(id, *params), ISimpleOverlay {

    override fun eval(list: PriceList): T {
        return eval(list.close)
    }

    abstract override fun eval(arr: FloatArray): T

    override fun getResultType(): Class<out ValueArray> {
        try {
            val evalMethod = javaClass.getMethod("eval", FloatArray::class.java)
            return evalMethod.returnType as Class<out ValueArray>
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }
}
