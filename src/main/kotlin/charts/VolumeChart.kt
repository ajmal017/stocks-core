package org.cerion.stocklist.charts

import org.cerion.stocklist.functions.ISimpleOverlay
import java.util.*

class VolumeChart : StockChart() {

    var logScale = false

    private val overlayDataSets: List<IDataSet>
        get() {
            resetNextColor()
            val result = ArrayList<IDataSet>()
            val volume = getPriceList(logScale).volume

            for (overlay in mOverlays) {
                val ol = overlay as ISimpleOverlay

                val arr = ol.eval(volume)
                result.addAll(getDefaultOverlayDataSets(arr, overlay))
            }

            return result
        }

    override val dataSets: List<IDataSet>
        get() {
        val result = ArrayList<IDataSet>()

        val data = DataSet(getPriceList(logScale).volume, "Volume", colorBlack())
        data.lineType = LineType.BAR
        result.addAll(Arrays.asList(data))

        result.addAll(overlayDataSets)
        return result
    }
}
