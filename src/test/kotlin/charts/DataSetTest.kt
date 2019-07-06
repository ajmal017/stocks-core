package org.cerion.stocklist.charts

import org.cerion.stocklist.arrays.FloatArray
import org.junit.Test

import org.junit.Assert.*

class DataSetTest {

    @Test
    fun sizeOffsetByOne() {
        val arr = FloatArray(5)
        val data = DataSet(arr, "", 0)

        assertEquals("size should be 1 less", (arr.size - 1).toLong(), data.size().toLong())
        assertEquals("invalid value at position 0", arr.get(1).toDouble(), data.get(0).toDouble(), 0.0001)
    }
}