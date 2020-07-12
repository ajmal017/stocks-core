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

import java.util.ArrayList

class IndicatorChart(private var mIndicator: IIndicator, colors: ChartColors = ChartColors()) : StockChart(colors) {

    private val extra = ArrayList<IIndicator>()

    var indicator: IIndicator
        get() = mIndicator
        set(indicator) {
            clearOverlays()
            mIndicator = indicator
        }

    val id: Indicator = mIndicator.id

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        val chart = super.clone() as IndicatorChart

        // Preserve overlays, they are reset with setIndicator()
        val overlays = ArrayList<IOverlay>()
        overlays.addAll(chart.mOverlays)

        // Copy indicator
        val params = indicator.params.toTypedArray().clone()
        val indicator = indicator.id.instance
        indicator.setParams(*params)
        chart.indicator = indicator

        // Add back overlays
        chart.mOverlays = overlays

        return chart
    }

    fun add(indicator: IIndicator) {
        if (indicator.id !== mIndicator.id)
            throw IllegalArgumentException("must be type " + mIndicator.id)

        extra.add(indicator)
    }

    override fun getDataSets(priceList: PriceList): List<IDataSet> {
        val result = ArrayList<IDataSet>()

        val arr = mIndicator.eval(priceList)
        result.addAll(getIndicatorDataSets(arr, mIndicator))

        // TODO set color on these
        for (indicator in extra) {
            val va = indicator.eval(priceList)
            result.addAll(getIndicatorDataSets(va, indicator))
        }

        result.addAll(getOverlayDataSets(arr))
        return result
    }

    private fun getOverlayDataSets(arr: ValueArray): List<DataSet> {
        resetNextColor()
        val result = ArrayList<DataSet>()

        for (overlay in mOverlays) {
            val ol = overlay as ISimpleOverlay

            val temp = ol.eval(arr as FloatArray)
            result.addAll(getDefaultOverlayDataSets(temp, ol))
        }

        return result
    }

    private fun getIndicatorDataSets(arr: ValueArray, indicator: IIndicator): List<DataSet> {
        // TODO look at all uses and see if any colors should be non-defaults (there are some that will)
        if (arr.javaClass == BandArray::class.java)
            throw NotImplementedError() // No indicators seem to be using this
        else if (arr.javaClass == MACDArray::class.java)
            return getMACDDataSet(arr as MACDArray, indicator.toString(), indicator.toString(), indicator.toString(), _colors.yellow, _colors.purple, _colors.secondaryBlue)
        else if (arr.javaClass == PairArray::class.java)
            return getPairDataSet(arr as PairArray, indicator.toString(), indicator.toString(), _colors.positiveGreen, _colors.negativeRed)

        return getSingleDataSet(arr as FloatArray, indicator.toString(), _colors.primary)
    }
}
