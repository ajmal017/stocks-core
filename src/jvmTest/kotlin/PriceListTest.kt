package org.cerion.stocks.core

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceListTest : TestBase() {

    @Test
    fun averageYearlyGain() {
        val gain = priceList.averageYearlyGain()
        assertEquals(0.0215, gain.toDouble(), 0.0001)
    }

    @Test
    fun generateSeries() {
        val list = PriceList.generateSeries(500)
        assertEquals(500, list.size)
        assertEquals(102.0f, list[0].close)
        assertEquals(319.43283f, list.last.close)
    }
}
