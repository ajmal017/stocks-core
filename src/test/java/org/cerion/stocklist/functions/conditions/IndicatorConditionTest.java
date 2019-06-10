package org.cerion.stocklist.functions.conditions;

import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.functions.IFunction;
import org.cerion.stocklist.indicators.RSI;
import org.cerion.stocklist.overlays.BollingerBands;
import org.cerion.stocklist.overlays.ExpMovingAverage;
import org.cerion.stocklist.overlays.KAMA;
import org.cerion.stocklist.overlays.SimpleMovingAverage;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndicatorConditionTest extends FunctionTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void verifies_floatArray() {
        new IndicatorCondition(new BollingerBands(), Condition.ABOVE, new ExpMovingAverage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifies_floatArray2() {
        new IndicatorCondition(new SimpleMovingAverage(), Condition.ABOVE, new BollingerBands());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifies_condition() {
        new IndicatorCondition(new SimpleMovingAverage(), Condition.INSIDE, new ExpMovingAverage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void verifies_insideBandArray() {
        new IndicatorCondition(0, Condition.INSIDE, new ExpMovingAverage());
    }

    @Test
    public void basic() {
        runFunction(new SimpleMovingAverage(20), Condition.BELOW, new ExpMovingAverage(50));
        runFunction(new ExpMovingAverage(50), Condition.ABOVE, new SimpleMovingAverage(20));
        runFunction(new RSI(7), Condition.BELOW, new RSI(14));
    }

    @Test
    public void bandArray() {
        runValue(2100, Condition.ABOVE, new BollingerBands(20,2));
        runValue(2050, Condition.INSIDE, new BollingerBands(20,2));
        runValue(2000, Condition.BELOW, new BollingerBands(20,2));
    }

    @Test
    public void floatArray_value() {
        runValue(43, Condition.ABOVE, new RSI(5));
        runValue(42, Condition.BELOW, new RSI(5));
    }

    @Test
    public void toStringTest() {
        assertEquals("EMA 20 above EMA 30", new IndicatorCondition(new ExpMovingAverage(20), Condition.ABOVE, new ExpMovingAverage(30)).toString());
        assertEquals("50.0 below KAMA 10,2,30", new IndicatorCondition(50.00f, Condition.BELOW, new KAMA()).toString());
        assertEquals("50.0 inside BB 20,2.0", new IndicatorCondition(50.00f, Condition.INSIDE, new BollingerBands()).toString());
    }

    private void runFunction(IFunction f1, Condition expectedCondition, IFunction f2) {
        for(Condition c : Condition.values()) {
            if (c == Condition.INSIDE) // not applicable to this constructor
                continue;

            if (c == expectedCondition) {
                assertTrue(new IndicatorCondition(f1, c, f2).eval(mPriceList));
                assertFalse(new IndicatorCondition(f2, c, f1).eval(mPriceList));
            }
            else {
                assertFalse(new IndicatorCondition(f1, c, f2).eval(mPriceList));
                assertTrue(new IndicatorCondition(f2, c, f1).eval(mPriceList));
            }
        }
    }

    private void runValue(float value, Condition expectedCondition, IFunction function) {
        for(Condition c : Condition.values()) {
            if (c == Condition.INSIDE && function.getResultType() != BandArray.class)
                continue;

            if (c == expectedCondition)
                assertTrue(new IndicatorCondition(value, c, function).eval(mPriceList));
            else
                assertFalse(new IndicatorCondition(value, c, function).eval(mPriceList));
        }
    }

}