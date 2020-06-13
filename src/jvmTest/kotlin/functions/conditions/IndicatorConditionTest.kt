package org.cerion.stocks.core.functions.conditions

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.functions.IFunction
import org.cerion.stocks.core.indicators.RSI
import org.cerion.stocks.core.overlays.BollingerBands
import org.cerion.stocks.core.overlays.ExpMovingAverage
import org.cerion.stocks.core.overlays.SimpleMovingAverage
import org.junit.Assert.*
import org.junit.Test

class IndicatorConditionTest : TestBase() {

    @Test(expected = IllegalArgumentException::class)
    fun verifies_floatArray() {
        IndicatorCondition(BollingerBands(), Condition.ABOVE, ExpMovingAverage())
    }

    @Test(expected = IllegalArgumentException::class)
    fun verifies_floatArray2() {
        IndicatorCondition(SimpleMovingAverage(), Condition.ABOVE, BollingerBands())
    }

    @Test(expected = IllegalArgumentException::class)
    fun verifies_condition() {
        IndicatorCondition(SimpleMovingAverage(), Condition.INSIDE, ExpMovingAverage())
    }

    @Test
    fun basic() {
        runFunction(SimpleMovingAverage(20), Condition.BELOW, ExpMovingAverage(50))
        runFunction(ExpMovingAverage(50), Condition.ABOVE, SimpleMovingAverage(20))
        runFunction(RSI(7), Condition.BELOW, RSI(14))
    }

    @Test
    fun toStringTest() {
        assertEquals("EMA 20 above EMA 30", IndicatorCondition(ExpMovingAverage(20), Condition.ABOVE, ExpMovingAverage(30)).toString())
    }

    private fun runFunction(f1: IFunction, expectedCondition: Condition, f2: IFunction) {
        for (c in Condition.values()) {
            if (c == Condition.INSIDE)
            // not applicable to this constructor
                continue

            if (c == expectedCondition) {
                assertTrue(IndicatorCondition(f1, c, f2).eval(priceList))
                assertFalse(IndicatorCondition(f2, c, f1).eval(priceList))
            } else {
                assertFalse(IndicatorCondition(f1, c, f2).eval(priceList))
                assertTrue(IndicatorCondition(f2, c, f1).eval(priceList))
            }
        }
    }
}