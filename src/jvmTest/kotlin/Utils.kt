package org.cerion.stocks.core

import kotlinx.coroutines.runBlocking
import org.cerion.stocks.core.model.Dividend
import org.cerion.stocks.core.platform.KMPDate
import org.cerion.stocks.core.web.CSVParser
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

actual suspend fun readResourceFile(fileName: String): String {
    return Utils.resourceToString(fileName)
}

actual fun runTest(block: suspend () -> Unit) = runBlocking {
    block()
}

object Utils {

    val sP500TestData: PriceList by lazy {
        val data = resourceToString("sp500_2000-2015.csv")
        PriceList("^GSPC", CSVParser.getPricesFromTable(data))
    }

    fun resourceToString(fileName: String): String {
        val classloader = Thread.currentThread().contextClassLoader
        val inputStream = classloader.getResourceAsStream(fileName)

        // Issue with getting resources in KMP project
        if (inputStream == null)
            return fileToString("src\\jvmTest\\resources\\$fileName")

        val isr = InputStreamReader(inputStream)
        val br = BufferedReader(isr)
        val sb = StringBuffer()
        for(line in br.lines())
            sb.append(line + "\r\n")

        return sb.toString()
    }

    private fun fileToString(fileName: String): String {
        return File(fileName).readText(Charsets.UTF_8)
        /*
        val contentBuilder = StringBuilder()

        Files.lines(Paths.get(fileName), StandardCharsets.UTF_8).use {
            stream -> stream.forEach { s: String? -> contentBuilder.append(s).append("\r\n") }
        }

        return contentBuilder.toString()
         */
    }

    fun generateList(size: Int): PriceList {
        val prices = ArrayList<PriceRow>()
        for (i in 0 until size)
            prices.add(PriceRow(KMPDate.TODAY, i.toFloat(), i.toFloat(), i.toFloat(), i.toFloat(), i.toFloat()))

        return PriceList("TEST", prices)
    }

    fun getDividends(vararg values: Float): List<Dividend> {
        val calendar = Calendar.getInstance()
        val result = ArrayList<Dividend>()

        for (v in values) {
            val d = Dividend(KMPDate(calendar.time), v)
            result.add(d)

            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        return result
    }

    fun getDate(daysAgo: Int): KMPDate {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -daysAgo)
        return KMPDate(calendar.time)
    }
}

