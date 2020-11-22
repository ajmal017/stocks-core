package org.cerion.stocks.core

import org.cerion.stocks.core.web.CSVParser
import kotlin.math.roundToInt
import kotlin.test.assertEquals


open class TestBase {

    fun assertEqual(expected: Float, actual: Float, message: String) {
        assertEquals(expected, actual, message)
    }

    fun assertEqual(expected: Double, actual: Float, message: String) {
        val dec2 = roundToDecimals(actual, 2).toString()
        assertEquals(expected.toString(), dec2, message)
    }

    fun assertEqual(expected: Double, actual: Float) {
        val dec2 = roundToDecimals(actual, 2).toString()
        assertEquals(expected.toString(), dec2)
    }

    protected fun runPriceTest(block: suspend (priceList: PriceList) -> Unit) = runAsync {
        if (!isInitialized()) {
            val data = readResourceFileAsync("sp500_2000-2015.csv").await()
            priceList2 = PriceList("^GSPC", CSVParser.getPricesFromTable(data))
        }

        block(priceList2)
    }

    fun roundToDecimals(value: Float, decimals: Int): Float {
        var dotAt = 1
        repeat(decimals) { dotAt *= 10 }
        val roundedValue = (value * dotAt).roundToInt()
        return (roundedValue.toFloat() / dotAt)// + (roundedValue % dotAt).toFloat() / dotAt
    }

    companion object {

        fun isInitialized() = ::priceList.isInitialized
        private lateinit var priceList2: PriceList

        // TODO remove this version
        lateinit var priceList: PriceList
        val size
            get() = priceList.size
    }
}