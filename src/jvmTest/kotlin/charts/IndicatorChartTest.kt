package org.cerion.stocks.core.charts

import org.cerion.stocks.core.indicators.MACD
import org.cerion.stocks.core.overlays.ExpMovingAverage
import org.cerion.stocks.core.overlays.SimpleMovingAverage
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


class IndicatorChartTest {

    @Test
    fun cloneTest() {
        val c1 = IndicatorChart(MACD(1, 2, 3))
        c1.addOverlay(SimpleMovingAverage(45))

        try {
            val c2 = c1.clone() as IndicatorChart

            assertEquals("indicator type", c1.id, c2.id)

            val i1 = c1.indicator
            val i2 = c2.indicator
            assertArrayEquals("parameters", i1.params.toTypedArray(), i2.params.toTypedArray())

            // Change original and verify cloned copy does not change
            c1.indicator.setParams(99, 2, 3)
            c1.getOverlay(0).setParams(39)
            c1.addOverlay(ExpMovingAverage(55))

            // Verify current
            assertEquals("original parameters", 99, i1.params[0])
            assertEquals("original overlay parameter", 39, c1.getOverlay(0).params[0])
            assertEquals("original 2nd overlay", 2, c1.overlayCount.toLong())

            // Verify clone
            assertEquals("cloned parameters", 1, i2.params[0])
            assertEquals("cloned overlay parameter", 45, c2.getOverlay(0).params[0])
            assertEquals("overlay count", 1, c2.overlayCount.toLong())


        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

    }

}