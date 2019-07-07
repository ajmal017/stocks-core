package org.cerion.stocklist.charts

import org.cerion.stocklist.Utils
import org.junit.Assert.assertEquals
import org.junit.Test


class CandleDataSetTest {

    @Test
    fun sizeOffsetByOne() {
        val list = Utils.sP500TestData
        val data = CandleDataSet(list, "", 0)

        assertEquals("size should be 1 less", (list.size - 1).toLong(), data.size.toLong())
        assertEquals("invalid close value at position 0", list.close(1).toDouble(), data.getClose(0).toDouble(), 0.0001)
        assertEquals("invalid high value at position 0", list.high(1).toDouble(), data.getHigh(0).toDouble(), 0.0001)
        assertEquals("invalid low value at position 0", list.low(1).toDouble(), data.getLow(0).toDouble(), 0.0001)
        assertEquals("invalid open value at position 0", list.mOpen.get(1).toDouble(), data.getOpen(0).toDouble(), 0.0001)
    }
}