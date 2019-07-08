package org.cerion.stocklist.model

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.Utils
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class DividendHistoryTest : TestBase() {

    @Test
    fun dates() {
        val dividends = Utils.getDividends(9.1f, 2.2f, 3.56f)
        val history = DividendHistory(dividends, Utils.getDate(30))

        assertEquals(9.1, history.lastDividend!!, 0.005)
        assertDateEquals(Date(), history.lastDividendDate!!)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        assertDateEquals(calendar.time, history.nextDividendEstimate!!)
    }

    @Test
    fun noDividends() {
        val history = DividendHistory(listOf(), GregorianCalendar(2011, 8, 13).time)

        assertNull(history.lastDividend)
        assertEquals(0.0, history.totalDividends, 0.0001)
        assertNull(history.lastDividendDate)
        assertNull(history.nextDividendEstimate)
    }

    @Test
    fun startDate_pastLastDividend() {
        val history = DividendHistory(getSampleList(), GregorianCalendar(2016, 5, 1).time)

        assertEquals(4.75, history.lastDividend!!, 0.0001)
        assertEquals(0.0, history.totalDividends, 0.0001)
        assertEquals(Date(1460444400000), history.lastDividendDate)
        assertEquals(Date(1467957600000), history.nextDividendEstimate)
    }

    @Test
    fun fields_Test() {
        val history = DividendHistory(getSampleList(), GregorianCalendar(2011, 8, 13).time)

        assertEquals(4.75, history.lastDividend!!, 0.0001)
        assertEquals(89.3, history.totalDividends, 0.0001)
        assertEquals(Date(1460444400000), history.lastDividendDate)
        assertEquals(Date(1467957600000), history.nextDividendEstimate)
    }

    private fun getSampleList(): List<Dividend> {
        val result = mutableListOf<Dividend>()

        val cal = GregorianCalendar(2010, 1, 1)

        for(i in 0..25) {
            cal.add(Calendar.DATE, 29 * 3) // Add 3 months but not exactly
            val date = cal.time

            result.add(Dividend(date, (i * 0.03f) + 4))
        }

        result.shuffle() // To test the fact this gets sorted in constructor
        return result
    }
}