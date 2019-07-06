package org.cerion.stocklist.functions.conditions

import org.cerion.stocklist.TestBase
import org.cerion.stocklist.arrays.BandArray
import org.cerion.stocklist.functions.IFunction
import org.cerion.stocklist.indicators.RSI
import org.cerion.stocklist.overlays.BollingerBands
import org.cerion.stocklist.overlays.ExpMovingAverage
import org.cerion.stocklist.overlays.KAMA
import org.junit.Assert
import org.junit.Test

class ValueIndicatorConditionTest : TestBase() {

    @Test(expected = IllegalArgumentException::class)
    fun verifies_insideBandArray() {
        ValueIndicatorCondition(0f, Condition.INSIDE, ExpMovingAverage())
    }

    @Test
    fun toStringTest() {
        Assert.assertEquals("50.0 below KAMA 10,2,30", ValueIndicatorCondition(50.00f, Condition.BELOW, KAMA()).toString())
        Assert.assertEquals("50.0 inside BB 20,2.0", ValueIndicatorCondition(50.00f, Condition.INSIDE, BollingerBands()).toString())
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

    private fun runValue(value: Float, expectedCondition: Condition, function: IFunction) {
        for (c in Condition.values()) {
            if (c == Condition.INSIDE && function.resultType != BandArray::class.java)
                continue

            if (c == expectedCondition)
                Assert.assertTrue(ValueIndicatorCondition(value, c, function).eval(priceList))
            else
                Assert.assertFalse(ValueIndicatorCondition(value, c, function).eval(priceList))
        }
    }
}