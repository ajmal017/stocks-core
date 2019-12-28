package org.cerion.stocks.core.charts

import org.cerion.stocks.core.arrays.FloatArray

class DataSet(private val values: FloatArray, override val label: String, override var color: Int) : IDataSet {

    // TODO make this mappable in kotlin for .map and .mapIndexed
    override var lineType = LineType.LINE
    override val size: Int = values.size - 1
    operator fun get(pos: Int): Float = values[pos + 1]

}
