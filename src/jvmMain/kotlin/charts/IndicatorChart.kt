package org.cerion.stocks.core.charts

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.BandArray
import org.cerion.stocks.core.arrays.FloatArray
import org.cerion.stocks.core.arrays.MACDArray
import org.cerion.stocks.core.arrays.PairArray
import org.cerion.stocks.core.arrays.ValueArray
import org.cerion.stocks.core.functions.IIndicator
import org.cerion.stocks.core.functions.IOverlay
import org.cerion.stocks.core.functions.ISimpleOverlay
import org.cerion.stocks.core.functions.types.Indicator

class IndicatorChart(indicator: IIndicator, colors: ChartColors = ChartColors()) : StockChart(colors) {

    private val extra = ArrayList<IIndicator>()

    var indicator: IIndicator = indicator
        set(value) {
            clearOverlays()
            field = value
        }

    val id: Indicator
        get() = indicator.id

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        val chart = super.clone() as IndicatorChart

        // Preserve overlays, they are reset with setIndicator()
        val overlays = ArrayList<IOverlay>()
        overlays.addAll(chart._overlays)

        // Copy indicator
        val params = indicator.params.toTypedArray().clone()
        val indicator = indicator.id.instance
        indicator.setParams(*params)
        chart.indicator = indicator

        // Add back overlays
        chart._overlays = overlays

        return chart
    }

    override fun getSerializedParams(): Map<String, String> {
        return mapOf(Pair("indicator", indicator.serialize()))
    }

    override fun setSerializedParams(params: Map<String, String>) {
        // No extra fields to set
    }

    fun add(extraIndicator: IIndicator) {
        if (indicator.id !== extraIndicator.id)
            throw IllegalArgumentException("must be type " + indicator.id)

        extra.add(extraIndicator)
    }

    override fun getDataSets(priceList: PriceList): List<IDataSet> {
        val result = mutableListOf<IDataSet>()

        val arr = indicator.eval(priceList)
        result += getIndicatorDataSets(arr, indicator)

        // TODO set color on these
        for (indicator in extra) {
            val va = indicator.eval(priceList)
            result += getIndicatorDataSets(va, indicator)
        }

        // Pass color of first data set to be ignored for any overlays
        result += getOverlayDataSets(arr, result[0].color)
        return result
    }

    private fun getOverlayDataSets(arr: ValueArray, ignoreColor: Int): List<DataSet> {
        resetNextColor()
        val result = mutableListOf<DataSet>()

        for (overlay in _overlays) {
            val ol = overlay as ISimpleOverlay

            val temp = ol.eval(arr as FloatArray)
            result += getDefaultOverlayDataSets(temp, ol, ignoreColor)
        }

        return result
    }

    private fun getIndicatorDataSets(arr: ValueArray, indicator: IIndicator): List<DataSet> {
        val label = indicator.toString()

        // TODO look at all uses and see if any colors should be non-defaults (there are some that will)
        return when (arr) {
            is BandArray -> throw NotImplementedError() // No indicators seem to be using this
            is MACDArray -> arr.getDataSets(label, label, label, _colors.primaryPurple, _colors.orange, _colors.secondaryBlue)
            is PairArray -> arr.getDataSets(label, label, _colors.positiveGreen, _colors.negativeRed)
            is FloatArray -> {
                // TODO add more special cases
                var color = _colors.primary
                if (indicator.id == Indicator.RSI)
                    color = _colors.primaryPurple

                listOf(arr.toDataSet(label, color))
            }
            else -> throw NotImplementedError()
        }
    }
}
