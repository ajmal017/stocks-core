package org.cerion.stocklist.functions

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.arrays.ValueArray
import org.cerion.stocklist.functions.types.IFunctionEnum

import java.util.Arrays

abstract class FunctionBase protected constructor(override val id: IFunctionEnum, vararg params: Number) : IFunction {

    private var mParams: Array<Number>? = null

    init {
        mParams = removeDoubles(*params)
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + id.ordinal
        result = prime * result + Arrays.hashCode(mParams)
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

        return if (id !== other.id) false else Arrays.equals(mParams, other.mParams)

        // Finally, equal if parameters are equal
    }

    override fun toString(): String {
        // String.join isn't working on android with a Set, possibly java v7/8 issue
        var join = id.toString()

        var i = 0
        for (n in mParams!!) {
            if (i > 0)
                join += ","
            else
                join += " "
            join += n
            i++
        }

        return join
    }

    override fun params(): Array<Number>? {
        return mParams
    }

    protected fun getFloat(pos: Int): Float {
        return mParams!![pos].toFloat()
    }

    protected fun getInt(pos: Int): Int {
        return mParams!![pos].toInt()
    }

    override fun setParams(vararg params: Number) {
        if (mParams!!.size != params.size)
            throw IllegalArgumentException("invalid parameter count")

        val newParams = removeDoubles(*params)

        for (i in newParams.indices) {
            if (newParams[0].javaClass != mParams!![0].javaClass)
                throw IllegalArgumentException("invalid parameter type at position $i")
        }

        mParams = newParams
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

    private fun removeDoubles(vararg params: Number): Array<Number> {
        val result = params.toMutableList()

        for (i in result.indices) {
            if (result[i].javaClass == Double::class.java)
                result[i] = params[i].toFloat()
        }

        return result.toTypedArray()
    }
}
