package org.cerion.stocklist

import org.junit.Assert.assertEquals
import org.junit.Test

class PriceListTest : TestBase() {

    @Test
    fun averageYearlyGain() {
        val gain = priceList.averageYearlyGain()
        assertEquals(0.0215, gain.toDouble(), 0.0001)
    }
}
