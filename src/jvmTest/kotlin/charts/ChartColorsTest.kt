package charts

import org.cerion.stocks.core.TestBase
import org.cerion.stocks.core.charts.ChartColors
import org.cerion.stocks.core.charts.IndicatorChart
import org.cerion.stocks.core.charts.PriceChart
import org.cerion.stocks.core.charts.VolumeChart
import org.cerion.stocks.core.indicators.AccumulationDistributionLine
import org.cerion.stocks.core.indicators.MACD
import org.cerion.stocks.core.indicators.Vortex
import org.junit.Assert.*
import org.junit.Test

class ChartColorsTest : TestBase() {

    private val colors = ChartColors().apply {
        primary = 1
        primaryBlue = 2
        volumneBlue = 3
        positiveGreen = 4
        negativeRed = 5

        orange = 6
        purple = 7
        yellow = 8
        secondaryBlue = 9
        secondaryRed = 10
        secondaryGreen = 11
    }

    @Test
    fun chartColors_basicCharts() {
        // Price chart is primary blue
        val priceChart = PriceChart(colors)
        var data = priceChart.getDataSets(priceList)
        assertEquals(2, data[0].color)

        // Misc line chart is primary
        val indicatorChart = IndicatorChart(AccumulationDistributionLine(), colors)
        data = indicatorChart.getDataSets(priceList)
        assertEquals(1, data[0].color)

        // Volume chart
        val volumeChart = VolumeChart(colors)
        data = volumeChart.getDataSets(priceList)
        assertEquals(3, data[0].color)

        // Pair
        val pairChart = IndicatorChart(Vortex(), colors)
        data = pairChart.getDataSets(priceList)
        assertEquals(4, data[0].color)
        assertEquals(5, data[1].color)
    }

    @Test
    fun chartColors_MACD() {
        val chart = IndicatorChart(MACD(), colors)
        val data = chart.getDataSets(priceList)
        assertEquals(8, data[0].color)
        assertEquals(7, data[1].color)
        assertEquals(9, data[2].color)
    }
}