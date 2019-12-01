package org.cerion.stocks.core.functions.types

import org.cerion.stocks.core.functions.IFunction

interface IFunctionEnum {
    val instance: IFunction
    val ordinal: Int
    fun getInstance(vararg params: Number): IFunction
}
