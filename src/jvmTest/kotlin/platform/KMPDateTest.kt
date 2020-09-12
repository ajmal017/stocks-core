package platform

import org.cerion.stocks.core.platform.KMPDate
import org.junit.Assert.*
import org.junit.Test
import java.util.*
import kotlin.test.asserter

class KMPDateTest {

    @Test
    fun isoDate() {
        assertEquals("2020-01-01", KMPDate(2020, 1, 1).toISOString())
        assertEquals("2020-02-29", KMPDate(2020, 2, 29).toISOString())
        assertEquals("2020-07-06", KMPDate(2020, 7, 6).toISOString())
        assertEquals("2020-12-31", KMPDate(2020, 12, 31).toISOString())
    }

    @Test
    fun add() {
        val start = KMPDate(2018, 9, 11)
        assertEquals("2018-09-16", start.add(5).toISOString())
        assertEquals("2018-09-30", start.add(19).toISOString())
        assertEquals("2018-10-01", start.add(20).toISOString())

        assertEquals("2018-09-06", start.add(-5).toISOString())
        assertEquals("2018-09-01", start.add(-10).toISOString())
        assertEquals("2018-08-31", start.add(-11).toISOString())
    }

    @Test
    fun diff() {
        val date1 = KMPDate(2018, 9, 11)
        val date2 = KMPDate(2018, 9, 10)
        val date3 = KMPDate(2018, 8, 11)

        assertEquals(1, date1.diff(date2))
        assertEquals(31, date1.diff(date3))
        assertEquals(-31, date3.diff(date1))
    }
}