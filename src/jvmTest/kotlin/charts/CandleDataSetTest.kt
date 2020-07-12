package org.cerion.stocks.core.charts

import org.cerion.stocks.core.Utils
import org.junit.Assert.assertEquals
import org.junit.Test


class CandleDataSetTest {

    @Test
    fun sizeOffsetByOne() {
        val list = Utils.sP500TestData
        val data = CandleDataSet(list, "")

        assertEquals("size should be 1 less", (list.size - 1).toLong(), data.size.toLong())
        assertEquals("invalid close value at position 0", list.close[1], data.getClose(0), 0.0001f)
        assertEquals("invalid high value at position 0", list.high[1], data.getHigh(0), 0.0001f)
        assertEquals("invalid low value at position 0", list.low[1], data.getLow(0), 0.0001f)
        assertEquals("invalid open value at position 0", list.open[1], data.getOpen(0), 0.0001f)
    }
}