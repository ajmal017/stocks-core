package org.cerion.stocklist.charts

import org.cerion.stocklist.arrays.FloatArray

class DataSet(private val values: FloatArray, override val label: String, override var color: Int) : IDataSet {

    override var lineType = LineType.LINE
    override val size: Int = values.size - 1
    operator fun get(pos: Int): Float = values[pos + 1]

}
