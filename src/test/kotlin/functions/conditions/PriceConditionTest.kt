package org.cerion.stocks.core.functions.conditions

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.arrays.BandArray
import org.cerion.stocks.core.functions.IPriceOverlay
import org.cerion.stocks.core.overlays.BollingerBands
import org.cerion.stocks.core.overlays.SimpleMovingAverage
import org.junit.Assert.*
import org.junit.Test


class PriceConditionTest : TestBase() {

    @Test(expected = IllegalArgumentException::class)
    fun verifiesCondition() {
        PriceCondition(Condition.INSIDE, SimpleMovingAverage())
    }

    @Test
    fun floatOverlay() {
        testCondition(Condition.BELOW, SimpleMovingAverage(20))
        testCondition(Condition.ABOVE, SimpleMovingAverage(94))
    }

    @Test
    fun bandOverlay() {
        testCondition(Condition.INSIDE, BollingerBands(20, 2.0))
        testCondition(Condition.ABOVE, BollingerBands(94, 0.2))
        testCondition(Condition.BELOW, BollingerBands(30, 0.2))
    }

    @Test
    fun toStringTest() {
        assertEquals("Price above SMA 29", PriceCondition(Condition.ABOVE, SimpleMovingAverage(29)).toString())
        assertEquals("Price below SMA 29", PriceCondition(Condition.BELOW, SimpleMovingAverage(29)).toString())
        assertEquals("Price inside BB 30,3.0", PriceCondition(Condition.INSIDE, BollingerBands(30, 3.0)).toString())
    }

    private fun testCondition(trueCondition: Condition, overlay: IPriceOverlay) {
        for (c in Condition.values()) {
            if (c == Condition.INSIDE && overlay.resultType != BandArray::class.java)
                continue

            val condition = PriceCondition(c, overlay)
            if (c == trueCondition)
                assertTrue("Price $trueCondition with $overlay", condition.eval(priceList))
            else
                assertFalse("Price NOT $trueCondition with $overlay", condition.eval(priceList))
        }
    }
}