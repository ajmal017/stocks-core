package org.cerion.stocklist.functions

import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.IFunctionEnum
import java.util.*

abstract class FunctionBase protected constructor(override val id: IFunctionEnum, vararg params: Number) : IFunction {

    private val _params: MutableList<Number>

    init {
        _params = removeDoubles(*params)
    }

    override val params: List<Number> = _params

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + id.ordinal
        result = prime * result + _params.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (other !is FunctionBase)
            return false
        if (javaClass != other.javaClass)
            return false

        return if (id !== other.id) false else _params == other._params

        // Finally, equal if parameters are equal
    }

    override fun toString(): String {
        // String.join isn't working on android with a Set, possibly java v7/8 issue
        var join = id.toString()

        var i = 0
        for (n in _params) {
            if (i > 0)
                join += ","
            else
                join += " "
            join += n
            i++
        }

        return join
    }

    protected fun getFloat(pos: Int): Float {
        return _params[pos].toFloat()
    }

    protected fun getInt(pos: Int): Int {
        return _params[pos].toInt()
    }

    override fun setParams(vararg params: Number) {
        if (_params.size != params.size)
            throw IllegalArgumentException("invalid parameter count")

        val newParams = removeDoubles(*params)

        for (i in newParams.indices) {
            val c1 = newParams[i].javaClass
            val c2 = _params[i].javaClass
            if (c1 != c2)
                throw IllegalArgumentException("invalid parameter type at position $i")
        }

        Collections.copy(_params, newParams)
    }

    override val resultType: Class<out ValueArray>
        get() {
            try {
                val methods = javaClass.methods.filter { it.name == "eval" && it.returnType.name != ValueArray::class.java.name }

                return methods.first().returnType as Class<out ValueArray>
                //val evalMethod = javaClass.getMethod("eval", FloatArray::class.java)
                //return evalMethod.returnType as Class<out ValueArray>
            } catch (e: Exception) {
                throw RuntimeException(e.message)
            }

        }

    private fun removeDoubles(vararg params: Number): MutableList<Number> {
        val result = params.toMutableList()

        for (i in result.indices) {
            if (result[i].javaClass == Double::class.java)
                result[i] = params[i].toFloat()
        }

        return result
    }
}
