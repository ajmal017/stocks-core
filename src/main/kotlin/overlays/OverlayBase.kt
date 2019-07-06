package org.cerion.stocklist.overlays


import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.FunctionBase
import org.cerion.stocklist.functions.IPriceOverlay
import org.cerion.stocklist.functions.ISimpleOverlay
import org.cerion.stocklist.functions.types.IFunctionEnum

abstract class PriceOverlayBase internal constructor(id: IFunctionEnum, vararg params: Number) : FunctionBase(id, *params), IPriceOverlay

abstract class OverlayBase<T : ValueArray> internal constructor(id: IFunctionEnum, vararg params: Number) : PriceOverlayBase(id, *params), ISimpleOverlay {

    override fun eval(list: PriceList): T {
        return eval(list.close)
    }

    abstract override fun eval(arr: FloatArray): T
}
