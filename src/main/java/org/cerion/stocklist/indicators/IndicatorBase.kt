package org.cerion.stocklist.indicators

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.FunctionBase
import org.cerion.stocklist.functions.IIndicator
import org.cerion.stocklist.functions.types.IFunctionEnum
import org.cerion.stocklist.functions.types.Indicator

abstract class IndicatorBase internal constructor(id: IFunctionEnum, vararg params: Number) : FunctionBase(id, *params), IIndicator {

    override fun getResultType(): Class<out ValueArray> {
        try {
            val evalMethod = javaClass.getMethod("eval", PriceList::class.java)
            return evalMethod.returnType as Class<out ValueArray>
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }

    }

    override fun getId(): Indicator {
        return super.getId() as Indicator
    }
}
