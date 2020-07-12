package org.cerion.stocks.core.charts

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.FloatArray
import org.cerion.stocks.core.functions.ISimpleOverlay
import java.util.*

class VolumeChart(colors: ChartColors = ChartColors()) : StockChart(colors) {
    var logScale = false

    private val barColor: Int
        get() = _colors.volumneBlue

    override fun getDataSets(priceList: PriceList): List<IDataSet> {
        val result = ArrayList<IDataSet>()
        val volume = if(logScale) priceList.toLogScale().volume else priceList.volume

        val data = DataSet(volume, "Volume", barColor)
        data.lineType = LineType.BAR
        result.addAll(listOf(data))

        val sets = getOverlayDataSets(volume)
        result.addAll(sets)
        return result
    }

    private fun getOverlayDataSets(volume: FloatArray): List<IDataSet> {
        resetNextColor()
        val result = ArrayList<IDataSet>()

        for (overlay in _overlays) {
            val ol = overlay as ISimpleOverlay

            val arr = ol.eval(volume)
            result.addAll(getDefaultOverlayDataSets(arr, overlay, barColor))
        }

        return result

    }

}
