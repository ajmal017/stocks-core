package org.cerion.stocks.core.charts

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.charts.IndicatorChart
import org.cerion.stocks.core.charts.PriceChart
import org.cerion.stocks.core.charts.VolumeChart
import org.cerion.stocks.core.indicators.Vortex
import org.cerion.stocks.core.overlays.BollingerBands
import org.cerion.stocks.core.overlays.ExpMovingAverage
import org.cerion.stocks.core.overlays.PriceChannels
import org.cerion.stocks.core.overlays.SimpleMovingAverage
import org.junit.Assert.assertEquals
import org.junit.Test

class StockChartTest : TestBase() {

    @Test
    fun dates_firstElementRemoved() {
        val chart = VolumeChart()
        val dates = chart.getDates(priceList)

        assertEquals(priceList.size - 1, dates.size)
        assertEquals(priceList.dates[1], dates[0])
        assertEquals(priceList.dates[priceList.size - 1], dates[dates.size - 1])
    }

    @Test
    fun dataSets_firstElementRemoved() {
        val chart = PriceChart()
        chart.addOverlay(SimpleMovingAverage())
        chart.addOverlay(BollingerBands())
        chart.addOverlay(PriceChannels())
        var dataSets = chart.getDataSets(priceList)

        val count = priceList.size - 1
        for(set in dataSets)
            assertEquals(count, set.size)

        val vchart = VolumeChart()
        vchart.addOverlay(ExpMovingAverage())
        dataSets = chart.getDataSets(priceList)
        for(set in dataSets)
            assertEquals(count, set.size)

        val ichart = IndicatorChart(Vortex())
        dataSets = ichart.getDataSets(priceList)
        for(set in dataSets)
            assertEquals(count, set.size)
    }

}