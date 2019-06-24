package org.cerion.stocklist.overlays

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.FunctionBase
import org.cerion.stocklist.functions.IPriceOverlay
import org.cerion.stocklist.functions.types.IFunctionEnum

abstract class PriceOverlayBase internal constructor(id: IFunctionEnum, vararg params: Number) : FunctionBase(id, *params), IPriceOverlay {

    override fun getResultType(): Class<out ValueArray> {
        try {
            val evalMethod = javaClass.getMethod("eval", PriceList::class.java)
            return evalMethod.returnType as Class<out ValueArray>
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }
}
