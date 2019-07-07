package org.cerion.stocklist.charts

import org.cerion.stocklist.functions.IOverlay
import org.cerion.stocklist.functions.types.IFunctionEnum
import org.cerion.stocklist.functions.types.Overlay
import org.cerion.stocklist.functions.types.PriceOverlay
import org.cerion.stocklist.model.Interval
import java.util.*

class PriceChart : StockChart() {

    var candleData = true
    var showPrice = true
    var logScale = false

    private val overlayDataSets: List<DataSet>
        get() {
            resetNextColor()
            val result = ArrayList<DataSet>()

            for (overlay in mOverlays) {
                val arr = overlay.eval(getPriceList(logScale))
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
        val overlay = Arrays.asList(*super.overlays)
        val priceOverlay = Arrays.asList(*PriceOverlay.values())

        val combined = ArrayList<IFunctionEnum>()
        combined.addAll(overlay)
        combined.addAll(priceOverlay)

        return combined.toTypedArray()
    }

    fun addOverlay(overlay: IOverlay) {
        mOverlays.add(overlay)
    }

    override val dataSets: List<IDataSet>
        get() {
        val result = ArrayList<IDataSet>()

        if (!showPrice) {
            // Don't add price data
        } else if (candleData && canShowCandleData()) {
            result.addAll(Arrays.asList(CandleDataSet(getPriceList(logScale), "Price", colorBlack())))
        } else {
            result.addAll(Arrays.asList(DataSet(getPriceList(logScale).close, "Price", colorBlack())))
        }

        result.addAll(overlayDataSets)

        return result
    }

    /**
     * Determines if this chart is able to display candle data, mutual funds on daily interval don't have high/low variation so candles shouldn't be used
     * @return true if this chart can display candle data properly
     */
    fun canShowCandleData(): Boolean {
        val list = priceList ?: return false

        // Only daily has this problem with high/low values
        if (list.interval != Interval.DAILY)
            return true

        for (i in list.indices) {
            if (list.high(i) != list.low(i))
                return true
        }

        return false
    }


}
