package org.cerion.stocklist.charts

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.functions.IOverlay
import org.cerion.stocklist.functions.types.IFunctionEnum
import org.cerion.stocklist.functions.types.PriceOverlay
import org.cerion.stocklist.model.Interval
import java.util.*

class PriceChart : StockChart() {
    var candleData = true
    var showPrice = true
    var logScale = false

    override fun getDataSets(priceList: PriceList): List<IDataSet> {
        val result = ArrayList<IDataSet>()
        val list = if(logScale) priceList.toLogScale() else priceList

        if (!showPrice) {
            // Don't add price data
        } else if (candleData && canShowCandleData(list)) {
            result.addAll(listOf(CandleDataSet(list, "Price", colorBlack())))
        } else {
            result.addAll(listOf(DataSet(list.close, "Price", colorBlack())))
        }

        result.addAll(getOverlayDataSets(list))
        return result
    }

    private fun getOverlayDataSets(list: PriceList): List<DataSet> {
        resetNextColor()
        val result = ArrayList<DataSet>()

        for (overlay in mOverlays) {
            val arr = overlay.eval(list)
            result.addAll(getDefaultOverlayDataSets(arr, overlay))

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
        mOverlays.add(overlay)
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
