package org.cerion.stocks.core.model

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.junit.Assert.assertEquals
import org.junit.Test
import org.cerion.stocks.core.platform.KMPDate
import java.util.*


class PositionValueTest {

    @Test
    fun priceChange() {
        val shares = 10.123f

        val pos = PositionWithDividends("SPY", shares.toDouble(), 2276.98, KMPDate(getDate(30)))
        val position = PositionValue(pos, null)
        val q = Quote("SPY")
        q.lastTrade = 2352.95f
        q.change = 1.1f
        q.changePercent = 2.2f
        position.quote = q

        // Check dividend values before adding them
        assertEquals(0.0, position.dividendProfit, 0.005)
        position.addDividends(getDividends(1.0f, 2.0f, 3.0f))

        // Check
        assertEquals(75.97, position.change, 0.005)
        assertEquals(0.0334, position.percentChanged, 0.00005)
        assertEquals(shares.toDouble(), position.currCount, 0.0)
        assertEquals(2352.95, position.currPrice, 0.005)
        assertEquals(2352.95 * shares, position.currValue, 0.005)

        assertEquals(769.05, position.profit, 0.005)
        assertEquals(10.123 * 6, position.dividendProfit, 0.005)
        assertEquals(0.0360, position.percentChangedWithDividends, 0.00005)
    }

    @Test
    fun priceChange_diviendsReinvested_FRIFX() {
        // Actual numbers from website
        //val lastPrice = 12.13f
        val currValue = 4154.87f
        val quantity = 342.529f
        //val purchasePrice = 4090.9401 // Issues representing this in double, should really be 4094.94000000000
        val profit = 63.95

        val pos = PositionWithDividends("FRIFX", 341.481, 11.98, KMPDate(GregorianCalendar(2017, 2, 9).time), true)
        val q = Quote("FRIFX")
        q.lastTrade = 12.13f

        //val api = YahooFinance.getInstance()
        val prices = ArrayList<PriceRow>()
        prices.add(PriceRow(KMPDate(GregorianCalendar(2017, 2, 8).time), 1f, 1f, 1f, 1f, 10000000.0f))
        prices.add(PriceRow(KMPDate(GregorianCalendar(2017, 2, 9).time), 11.99f, 11.99f, 11.90f, 11.943306f, 10000000.0f))
        prices.add(PriceRow(KMPDate(GregorianCalendar(2017, 2, 10).time), 2f, 2f, 2f, 2f, 10000000.0f))
        val list = PriceList("FRIFX", prices)

        val position = PositionValue(pos, list)
        position.quote = q

        // Gains
        assertEquals(0.0156316852, position.percentChanged, 0.000005)
        assertEquals(profit, position.profit, 0.005)
        assertEquals(quantity.toDouble(), position.currCount, 0.0005)
        assertEquals(currValue.toDouble(), position.currValue, 0.005)

        // Dividend
        assertEquals(0.0, position.dividendProfit, 0.005)
        assertEquals(position.percentChanged, position.percentChangedWithDividends, 0.005)
    }

    @Test
    fun priceChange_diviendsReinvested_somethingelse() {
        // TODO get another one from the start with additional values like Total gain/loss columns
    }

    @Test
    fun dividendHistory() {
        val pos = PositionWithDividends("SPY", 10.0, 123.45, KMPDate(getDate(30)))
        val position = PositionValue(pos, null)
        val q = Quote("SPY")
        q.lastTrade = 123.45f
        position.quote = q
        position.addDividends(getDividends(9.1f, 2.2f, 3.56f))

        assertEquals(0.0, position.profit, 0.005)
        assertEquals(10 * (9.1 + 2.2 + 3.56), position.dividendProfit, 0.005)

        val currentValue = position.dividendProfit + position.currValue
        val origValue = pos.origValue
        val change = (currentValue - origValue) / origValue

        assertEquals(change, position.percentChangedWithDividends, 0.005)
    }

    @Test
    fun stockSplit() {
        // TODO test for stock split, most likely will not work right
    }

    private fun getDividends(vararg values: Float): List<Dividend> {
        val calendar = Calendar.getInstance()
        val result = ArrayList<Dividend>()

        for (v in values) {
            val d = Dividend(KMPDate(calendar.time), v)
            result.add(d)

            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        return result
    }

    private fun getDate(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -daysAgo)
        return calendar.time
    }

}