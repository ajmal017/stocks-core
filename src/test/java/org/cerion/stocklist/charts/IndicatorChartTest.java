package org.cerion.stocklist.charts;

import org.cerion.stocklist.functions.IIndicator;
import org.cerion.stocklist.indicators.MACD;
import org.cerion.stocklist.overlays.ExpMovingAverage;
import org.cerion.stocklist.overlays.SimpleMovingAverage;
import org.junit.Test;

import static org.junit.Assert.*;


public class IndicatorChartTest {

    @Test
    public void cloneTest() {
        IndicatorChart c1 = new IndicatorChart(new MACD(1,2,3));
        c1.addOverlay(new SimpleMovingAverage(45));

        try {
            IndicatorChart c2 = (IndicatorChart) c1.clone();

            assertEquals("indicator type", c1.getId(), c2.getId());

            IIndicator i1 = c1.getIndicator();
            IIndicator i2 = c2.getIndicator();
            assertArrayEquals("parameters", i1.params(), i2.params());

            // Change original and verify cloned copy does not change
            c1.getIndicator().params()[0] = 99;
            c1.getOverlay(0).params()[0] = 39;
            c1.addOverlay(new ExpMovingAverage(55));

            // Verify current
            assertEquals("original parameters", 99, i1.params()[0]);
            assertEquals("original overlay parameter", 39, c1.getOverlay(0).params()[0]);
            assertEquals("original 2nd overlay", 2, c1.getOverlayCount());

            // Verify clone
            assertEquals("cloned parameters", 1, i2.params()[0]);
            assertEquals("cloned overlay parameter", 45, c2.getOverlay(0).params()[0]);
            assertEquals("overlay count", 1, c2.getOverlayCount());


        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

}