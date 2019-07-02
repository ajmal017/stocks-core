package org.cerion.stocklist.arrays

abstract class ValueArray {

    abstract val size: Int

    // TODO remove after other classes are refactored
    protected fun size(): Int {
        return size
    }

    companion object {
        //Use a lower period value when calculating array elements before that position so all values get set to something
        fun maxPeriod(pos: Int, period: Int): Int {
            return if (pos < period - 1) pos + 1 else period
        }
    }

}
