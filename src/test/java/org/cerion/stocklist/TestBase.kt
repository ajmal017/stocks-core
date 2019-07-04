package org.cerion.stocklist

import org.junit.Assert
import org.junit.Assert.assertEquals
import java.util.*

open class TestBase {

    fun assertEqual(expected: Double, actual: Float, message: String) {
        assertEquals(message, expected, actual.toDouble(), 0.005)
    }

    fun assertDateEquals(expected: Date, actual: Date) {
        Assert.assertEquals(expected.time / 1000, actual.time / 1000)
    }

    companion object {
        val priceList: PriceList by lazy {
            Helper.getSP500TestData()
        }

        val size: Int by lazy {
            priceList.size
        }
    }
}
