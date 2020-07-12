package org.cerion.stocks.core.charts

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.*
import org.cerion.stocks.core.arrays.FloatArray
import org.cerion.stocks.core.functions.IOverlay
import org.cerion.stocks.core.functions.ISimpleOverlay
import org.cerion.stocks.core.functions.types.IFunctionEnum
import org.cerion.stocks.core.functions.types.Overlay
import org.cerion.stocks.core.platform.KMPDate
import java.util.*

abstract class StockChart(protected val _colors: ChartColors) : Cloneable {

    protected var _overlays: MutableList<IOverlay> = ArrayList()
    private var _nextColor = 0

    open val overlays: Array<IFunctionEnum>
        get() = Overlay.values().toList().toTypedArray()

    val overlayCount: Int
        get() = _overlays.size

    abstract fun getDataSets(priceList: PriceList): List<IDataSet>

    fun getDates(list: PriceList): Array<KMPDate> = list.dates.sliceArray(1 until list.dates.size)

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        val clone = super.clone() as StockChart

        // Copy overlays
        clone._overlays = ArrayList()

        for (overlay in _overlays) {
            val copy = overlay.id.instance
            copy.setParams(*overlay.params.toTypedArray().clone())
            clone._overlays.add(copy as IOverlay)
        }

        return clone
    }

    fun addOverlay(overlay: ISimpleOverlay): StockChart {
        _overlays.add(overlay)
        return this
    }

    fun clearOverlays() {
        _overlays.clear()
    }

    fun getOverlay(position: Int): IOverlay = _overlays[position]

    private fun getNextColor(ignoreColor: Int): Int {
        val color = _colors.getOverlayColor(_nextColor++)

        if (color == ignoreColor && color != 0) // Ignore zero for unit tests that don't set color values
            return getNextColor((ignoreColor))

        return color
    }

    protected fun resetNextColor() {
        _nextColor = 0
    }

    protected fun getDefaultOverlayDataSets(arr: ValueArray, overlay: IOverlay, ignoreColor: Int): List<DataSet> {
        if (arr.javaClass == BandArray::class.java)
            return getBandDataSet(arr as BandArray, overlay.toString(), overlay.toString(), getNextColor(ignoreColor))
        else if (arr.javaClass == PairArray::class.java) // TODO these are more complex for colors, look into later, using primary as placeholder
            return getPairDataSet(arr as PairArray, overlay.toString(), overlay.toString(), _colors.primary, _colors.primary)

        return getSingleDataSet(arr as FloatArray, overlay.toString(), getNextColor(ignoreColor))
    }

    protected fun getSingleDataSet(values: FloatArray, label: String, color: Int): List<DataSet> {
        return ArrayList(listOf(DataSet(values, label, color)))
    }

    private fun getBandDataSet(values: BandArray, labelUpper: String, labelLower: String, color: Int): List<DataSet> {
        val upper = FloatArray(values.size)
        val lower = FloatArray(values.size)

        for (i in 0 until values.size) {
            upper[i] = values.upper(i)
            lower[i] = values.lower(i)
        }

        val result = ArrayList<DataSet>()
        result.add(DataSet(upper, labelUpper, color))
        result.add(DataSet(lower, labelLower, color))

        return result
    }

    protected fun getPairDataSet(values: PairArray, labelPos: String, labelNeg: String, colorPos: Int, colorNeg: Int): List<DataSet> {
        val result = ArrayList<DataSet>()
        result.add(DataSet(values.positive, labelPos, colorPos))
        result.add(DataSet(values.negative, labelNeg, colorNeg))
        return result
    }

    protected fun getMACDDataSet(values: MACDArray,
                                 labelMACD: String, labelSignal: String, labelHist: String,
                                 colorMACD: Int, colorSignal: Int, colorHist: Int): List<DataSet> {

        val signal = FloatArray(values.size)
        val hist = FloatArray(values.size)

        // TODO make function to get signal/hist arrays directly
        for (i in 0 until values.size) {
            signal[i] = values.signal(i)
            hist[i] = values.hist(i)
        }

        val result = ArrayList<DataSet>()
        result.add(DataSet(values, labelMACD, colorMACD))
        result.add(DataSet(signal, labelSignal, colorSignal))
        result.add(DataSet(hist, labelHist, colorHist))

        return result
    }
}
