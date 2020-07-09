package platform

import org.cerion.stocks.core.platform.KMPDate
import org.junit.Assert.*
import org.junit.Test

class KMPDateTest {

    @Test
    fun isoDate() {
        assertEquals("2020-01-01", KMPDate(2020, 1, 1).toISOString())
        assertEquals("2020-02-29", KMPDate(2020, 2, 29).toISOString())
        assertEquals("2020-07-06", KMPDate(2020, 7, 6).toISOString())
        assertEquals("2020-12-31", KMPDate(2020, 12, 31).toISOString())
    }
}