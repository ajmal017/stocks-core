package org.cerion.stocks.core.charts

import org.cerion.stocks.core.arrays.FloatArray

class DataSet(private val values: FloatArray, override val label: String, override var color: Int) : IDataSet, Iterable<Float> {

    override var lineType = LineType.LINE
    override val size: Int = values.size - 1

    @Deprecated("Use iterator") // Not sure if this can completely go
    operator fun get(pos: Int): Float = values[pos + 1]

    override fun iterator(): Iterator<Float> {
        return object : Iterator<Float> {
            private var index = 1 // Always ignore first element in FloatArray
            override fun hasNext(): Boolean = index < values.size
            override fun next(): Float = values[index++]
        }
    }
}