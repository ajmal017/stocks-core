package org.cerion.stocklist.functions.conditions

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.arrays.BandArray
import org.cerion.stocklist.functions.IFunction
import org.cerion.stocklist.indicators.RSI
import org.cerion.stocklist.overlays.BollingerBands
import org.cerion.stocklist.overlays.ExpMovingAverage
import org.cerion.stocklist.overlays.KAMA
import org.cerion.stocklist.overlays.SimpleMovingAverage
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

    @Test(expected = IllegalArgumentException::class)
    fun verifies_insideBandArray() {
        IndicatorCondition(0f, Condition.INSIDE, ExpMovingAverage())
    }

    @Test
    fun basic() {
        runFunction(SimpleMovingAverage(20), Condition.BELOW, ExpMovingAverage(50))
        runFunction(ExpMovingAverage(50), Condition.ABOVE, SimpleMovingAverage(20))
        runFunction(RSI(7), Condition.BELOW, RSI(14))
    }

    @Test
    fun bandArray() {
        runValue(2100f, Condition.ABOVE, BollingerBands(20, 2.0))
        runValue(2050f, Condition.INSIDE, BollingerBands(20, 2.0))
        runValue(2000f, Condition.BELOW, BollingerBands(20, 2.0))
    }

    @Test
    fun floatArray_value() {
        runValue(43f, Condition.ABOVE, RSI(5))
        runValue(42f, Condition.BELOW, RSI(5))
    }

    @Test
    fun toStringTest() {
        assertEquals("EMA 20 above EMA 30", IndicatorCondition(ExpMovingAverage(20), Condition.ABOVE, ExpMovingAverage(30)).toString())
        assertEquals("50.0 below KAMA 10,2,30", IndicatorCondition(50.00f, Condition.BELOW, KAMA()).toString())
        assertEquals("50.0 inside BB 20,2.0", IndicatorCondition(50.00f, Condition.INSIDE, BollingerBands()).toString())
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

    private fun runValue(value: Float, expectedCondition: Condition, function: IFunction) {
        for (c in Condition.values()) {
            if (c == Condition.INSIDE && function.resultType != BandArray::class.java)
                continue

            if (c == expectedCondition)
                assertTrue(IndicatorCondition(value, c, function).eval(priceList))
            else
                assertFalse(IndicatorCondition(value, c, function).eval(priceList))
        }
    }

}