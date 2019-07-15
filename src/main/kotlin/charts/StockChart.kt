package org.cerion.stocklist.charts

import org.cerion.stocklist.PriceList
import org.cerion.stocklist.arrays.*
import org.cerion.stocklist.arrays.FloatArray
import org.cerion.stocklist.functions.IOverlay
import org.cerion.stocklist.functions.ISimpleOverlay
import org.cerion.stocklist.functions.types.IFunctionEnum
import org.cerion.stocklist.functions.types.Overlay
import java.util.*

abstract class StockChart : Cloneable {

    //protected List<FunctionCall> mOverlays = new ArrayList<>();
    protected var mOverlays: MutableList<IOverlay> = ArrayList()
    private var nextColor = 0

    protected var mPrimaryColors = intArrayOf(0, 0, 0, 0) // TODO add colors that don't use any java libraries, android overrides them all anyway
    protected var mSecondaryColors = intArrayOf(0)

    open val overlays: Array<IFunctionEnum>
        get() = Overlay.values().toList().toTypedArray()

    val overlayCount: Int
        get() = mOverlays.size

    abstract fun getDataSets(priceList: PriceList): List<IDataSet>

    fun getDates(list: PriceList): Array<Date> = list.dates.sliceArray(1 until list.dates.size)

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        val clone = super.clone() as StockChart

        // Copy overlays
        clone.mOverlays = ArrayList()

        for (overlay in mOverlays) {
            val copy = overlay.id.instance
            copy.setParams(*overlay.params.toTypedArray().clone())
            clone.mOverlays.add(copy as IOverlay)
        }

        return clone
    }

    fun addOverlay(overlay: ISimpleOverlay): StockChart {
        mOverlays.add(overlay)
        return this
    }

    fun clearOverlays() {
        mOverlays.clear()
    }

    fun getOverlay(position: Int): IOverlay = mOverlays[position]

    protected fun colorBlack(): Int = mPrimaryColors[0]
    protected fun colorRed(): Int = mPrimaryColors[1]
    protected fun colorBlue(): Int = mPrimaryColors[2]
    protected fun colorGreen(): Int = mPrimaryColors[3]

    fun setPrimaryColors(colors: IntArray) {
        if (colors.size == mPrimaryColors.size)
            mPrimaryColors = colors
    }

    fun setSecondaryColors(colors: IntArray) {
        mSecondaryColors = colors
    }

    //protected fun getPriceList(logScale: Boolean): PriceList {
    //    return if (logScale) mList!!.toLogScale() else mList!!
    //}

    protected fun getNextColor(): Int {
        return mSecondaryColors[nextColor++ % mSecondaryColors.size]
    }

    protected fun resetNextColor() {
        nextColor = 0
    }

    protected fun getDefaultOverlayDataSets(arr: ValueArray, overlay: IOverlay): List<DataSet> {
        if (arr.javaClass == BandArray::class.java)
            return getBandDataSet(arr as BandArray, overlay.toString(), overlay.toString(), getNextColor())
        else if (arr.javaClass == PairArray::class.java)
            return getPairDataSet(arr as PairArray, overlay.toString(), overlay.toString(), colorGreen(), colorRed())

        return getSingleDataSet(arr as FloatArray, overlay.toString(), getNextColor())
    }

    protected fun getSingleDataSet(values: FloatArray, label: String, color: Int): List<DataSet> {
        return ArrayList(listOf(DataSet(values, label, color)))
    }

    protected fun getBandDataSet(values: BandArray, labelUpper: String, labelLower: String, color: Int): List<DataSet> {
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
