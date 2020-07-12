package org.cerion.stocks.core.charts

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.functions.IOverlay
import org.cerion.stocks.core.functions.types.IFunctionEnum
import org.cerion.stocks.core.functions.types.PriceOverlay
import org.cerion.stocks.core.model.Interval
import java.util.*

class PriceChart(colors: ChartColors = ChartColors()) : StockChart(colors) {
    var candleData = false
    var showPrice = true
    var logScale = false

    private val lineColor: Int
        get() = _colors.primaryBlue

    override fun getDataSets(priceList: PriceList): List<IDataSet> {
        val result = ArrayList<IDataSet>()
        val list = if(logScale) priceList.toLogScale() else priceList

        if (!showPrice) {
            // Don't add price data
        } else if (candleData && canShowCandleData(list)) {
            result.addAll(listOf(CandleDataSet(list, "Price")))
        } else {
            result.addAll(listOf(DataSet(list.close, "Price", lineColor)))
        }

        result.addAll(getOverlayDataSets(list))
        return result
    }

    private fun getOverlayDataSets(list: PriceList): List<DataSet> {
        resetNextColor()
        val result = ArrayList<DataSet>()

        for (overlay in _overlays) {
            val arr = overlay.eval(list)
            result.addAll(getDefaultOverlayDataSets(arr, overlay, lineColor))

            if (overlay.id.javaClass == PriceOverlay::class.java) {
                val ol = overlay.id as PriceOverlay

                if (ol == PriceOverlay.PSAR)
                    result[result.size - 1].lineType = LineType.DOTTED
            }
        }

        return result
    }

    override val overlays: Array<IFunctionEnum>
        get() {
        val overlay = listOf(*super.overlays)
        val priceOverlay = listOf(*PriceOverlay.values())

        val combined = ArrayList<IFunctionEnum>()
        combined.addAll(overlay)
        combined.addAll(priceOverlay)

        return combined.toTypedArray()
    }

    fun addOverlay(overlay: IOverlay) {
        _overlays.add(overlay)
    }

    /**
     * Determines if this chart is able to display candle data, mutual funds on daily interval don't have high/low variation so candles shouldn't be used
     * @return true if this chart can display candle data properly
     */
    fun canShowCandleData(list: PriceList): Boolean {
        // Only daily has this problem with high/low values
        if (list.interval != Interval.DAILY)
            return true

        for (i in list.indices) {
            if (list.high[i] != list.low[i])
                return true
        }

        return false
    }
}
