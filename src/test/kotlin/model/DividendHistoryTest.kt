package org.cerion.stocklist.model

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.Utils
import org.junit.Test

import java.util.Calendar
import java.util.Date

import org.junit.Assert.*

class DividendHistoryTest : TestBase() {

    @Test
    fun dates() {
        val dividends = Utils.getDividends(9.1f, 2.2f, 3.56f)
        val history = DividendHistory(dividends, Utils.getDate(30))

        assertEquals(9.1, history.lastDividend, 0.005)
        assertDateEquals(Date(), history.lastDividendDate)

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        assertDateEquals(calendar.time, history.nextDividendEstimate)
    }
}