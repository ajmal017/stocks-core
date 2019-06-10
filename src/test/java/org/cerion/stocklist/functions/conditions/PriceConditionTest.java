package org.cerion.stocklist.functions.conditions;

import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.functions.IPriceOverlay;
import org.cerion.stocklist.functions.conditions.Condition;
import org.cerion.stocklist.functions.conditions.PriceCondition;
import org.cerion.stocklist.overlays.BollingerBands;
import org.cerion.stocklist.overlays.SimpleMovingAverage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PriceConditionTest extends FunctionTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void verifiesCondition() {
        new PriceCondition(Condition.INSIDE, new SimpleMovingAverage());
    }

    @Test
    public void floatOverlay() {
        testCondition(Condition.BELOW, new SimpleMovingAverage(20));
        testCondition(Condition.ABOVE, new SimpleMovingAverage(94));
    }

    @Test
    public void bandOverlay() {
        testCondition(Condition.INSIDE, new BollingerBands(20, 2));
        testCondition(Condition.ABOVE, new BollingerBands(94, 0.2));
        testCondition(Condition.BELOW, new BollingerBands(30, 0.2));
    }

    @Test
    public void toStringTest() {
        assertEquals("Price above SMA 29", new PriceCondition(Condition.ABOVE, new SimpleMovingAverage(29)).toString() );
        assertEquals("Price below SMA 29", new PriceCondition(Condition.BELOW, new SimpleMovingAverage(29)).toString() );
        assertEquals("Price inside BB 30,3.0", new PriceCondition(Condition.INSIDE, new BollingerBands(30, 3.0)).toString() );
    }

    private void testCondition(Condition trueCondition, IPriceOverlay overlay) {
        for(Condition c : Condition.values()) {
            if (c == Condition.INSIDE && overlay.getResultType() != BandArray.class)
                continue;

            PriceCondition condition = new PriceCondition(c, overlay);
            if (c == trueCondition)
                assertTrue("Price " + trueCondition + " with " + overlay, condition.eval(mPriceList));
            else
                assertFalse("Price NOT " + trueCondition + " with " + overlay, condition.eval(mPriceList));
        }
    }
}