package org.cerion.stocklist.web

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.web.api.YahooFinance
import org.junit.Test

class YahooFinanceTest : TestBase() {

    @Test
    fun parseLine_allPricesEqual() {
        val p = YahooFinance.parseLine("2017-02-17,12.01,12.01,12.01,12.01,12.01,000")

        assertEqual(12.01, p.open, "open")
        assertEqual(12.01, p.high, "high")
        assertEqual(12.01, p.low, "low")
        assertEqual(12.01, p.close, "close")
    }

    @Test
    fun parseLine_allPricesEqual_adjClose() {
        val p = YahooFinance.parseLine("2017-02-17,12.01,12.01,12.01,12.01,11.973215,000")

        assertEqual(11.973215, p.open)
        assertEqual(11.973215, p.high)
        assertEqual(11.973215, p.low)
        assertEqual(11.973215, p.close)
    }
}