package org.cerion.stocklist.functions.types

import org.cerion.stocklist.functions.IFunction

interface IFunctionEnum {
    val instance: IFunction
    val ordinal: Int
    fun getInstance(vararg params: Number): IFunction
}
