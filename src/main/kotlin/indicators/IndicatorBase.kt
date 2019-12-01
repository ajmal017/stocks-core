package org.cerion.stocks.core.indicators

import org.cerion.stocks.core.functions.FunctionBase
import org.cerion.stocks.core.functions.IIndicator
import org.cerion.stocks.core.functions.types.IFunctionEnum
import org.cerion.stocks.core.functions.types.Indicator

abstract class IndicatorBase internal constructor(id: IFunctionEnum, vararg params: Number) : FunctionBase(id, *params), IIndicator {
    override val id: Indicator = super.id as Indicator
}
