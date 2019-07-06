package org.cerion.stocklist.functions

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.IFunctionEnum
import org.cerion.stocklist.functions.types.Indicator


interface IFunction {
    val name: String
    val resultType: Class<out ValueArray>
    val id: IFunctionEnum
    val params: List<Number>
    fun eval(list: PriceList): ValueArray
    fun setParams(vararg params: Number)
}

interface IIndicator : IFunction {
    override val id: Indicator
}

interface IOverlay : IFunction

interface IPriceOverlay : IOverlay

interface ISimpleOverlay : IOverlay {
    fun eval(arr: FloatArray): ValueArray
}
