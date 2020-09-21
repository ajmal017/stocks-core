package org.cerion.stocks.core.functions

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.FloatArray
import org.cerion.stocks.core.arrays.ValueArray
import org.cerion.stocks.core.functions.types.IFunctionEnum
import org.cerion.stocks.core.functions.types.Indicator
import kotlin.reflect.KClass


interface IFunction {
    val name: String
    val resultType: KClass<*>
    val id: IFunctionEnum
    val params: List<Number>
    fun eval(list: PriceList): ValueArray
    fun setParams(vararg params: Number)
    fun serialize(): String
}

interface IIndicator : IFunction {
    override val id: Indicator
}

interface IOverlay : IFunction

interface IPriceOverlay : IOverlay

interface ISimpleOverlay : IOverlay {
    fun eval(arr: FloatArray): ValueArray
}
