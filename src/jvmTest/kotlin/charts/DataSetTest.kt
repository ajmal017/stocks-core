package org.cerion.stocks.core.charts

import org.cerion.stocks.core.arrays.FloatArray
import org.junit.Test

import org.junit.Assert.*

class DataSetTest {

    @Test
    fun sizeOffsetByOne() {
        val arr = FloatArray(5)
        val data = DataSet(arr, "", 0)

        assertEquals("size should be 1 less", arr.size - 1, data.size)
        assertEquals("invalid value at position 0", arr[1].toDouble(), data[0].toDouble(), 0.0001)
    }

    @Test
    fun dataSet_iterator() {
        val arr = FloatArray(5) // Length 5 but iterator skips first element and returns 4
        for(i in 0 until 5)
            arr[i] = (i * i).toFloat()

        val data = DataSet(arr, "", 0)
        val mapped = data.mapIndexed { index: Int, value: Float ->
            Pair(index, value)
        }

        assertEquals(4, mapped.size)
        assertEquals(Pair(0, 1.0f), mapped[0])
        assertEquals(Pair(3, 16.0f), mapped[3])
    }
}