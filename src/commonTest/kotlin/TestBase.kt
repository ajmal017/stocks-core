package org.cerion.stocks.core

import org.cerion.stocks.core.web.CSVParser
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.test.assertEquals


open class TestBase {

    @Deprecated("use precision methods")
    fun assertEqual(expected: Float, actual: Float, message: String) {
        assertEquals(expected, actual, message)
    }

    @Deprecated("use precision methods")
    fun assertEqual(expected: Double, actual: Float, message: String) {
        val dec2 = roundToDecimals(actual, 2).toString()
        assertEquals(expected.toString(), dec2, message)
    }

    @Deprecated("use precision methods")
    fun assertEqual(expected: Double, actual: Float) {
        val dec2 = roundToDecimals(actual, 2).toString()
        assertEquals(expected.toString(), dec2)
    }

    fun assertEquals(expected: Number, actual: Float, message: String? = null) =
            assertEquals(expected, actual, 0.005, message)

    fun assertEquals(expected: Number, actual: Float, delta: Double, message: String? = null) {
        val diff = abs(expected.toDouble() - actual)
        if (diff > delta)
            assertEquals(expected, actual, message)
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