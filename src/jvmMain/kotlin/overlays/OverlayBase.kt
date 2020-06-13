package org.cerion.stocks.core.overlays


import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.FloatArray
import org.cerion.stocks.core.arrays.ValueArray
import org.cerion.stocks.core.functions.FunctionBase
import org.cerion.stocks.core.functions.IPriceOverlay
import org.cerion.stocks.core.functions.ISimpleOverlay
import org.cerion.stocks.core.functions.types.IFunctionEnum

abstract class PriceOverlayBase internal constructor(id: IFunctionEnum, vararg params: Number) : FunctionBase(id, *params), IPriceOverlay

abstract class OverlayBase<T : ValueArray> internal constructor(id: IFunctionEnum, vararg params: Number) : PriceOverlayBase(id, *params), ISimpleOverlay {

    override fun eval(list: PriceList): T {
        return eval(list.close)
    }

    abstract override fun eval(arr: FloatArray): T
}
