package org.cerion.stocklist.indicators

import org.cerion.stocklist.functions.FunctionBase
import org.cerion.stocklist.functions.IIndicator
import org.cerion.stocklist.functions.types.IFunctionEnum
import org.cerion.stocklist.functions.types.Indicator

abstract class IndicatorBase internal constructor(id: IFunctionEnum, vararg params: Number) : FunctionBase(id, *params), IIndicator {
    override val id: Indicator = super.id as Indicator
}
