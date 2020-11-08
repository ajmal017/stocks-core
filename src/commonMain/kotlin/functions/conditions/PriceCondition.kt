package org.cerion.stocks.core.functions.conditions

import org.cerion.stocks.core.Price
import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.arrays.BandArray
import org.cerion.stocks.core.arrays.FloatArray
import org.cerion.stocks.core.charts.PriceChart
import org.cerion.stocks.core.charts.StockChart
import org.cerion.stocks.core.functions.IPriceOverlay

class PriceCondition(private val condition: Condition, private val overlay: IPriceOverlay) : ICondition {

    override val chart: StockChart
        get() {
            val chart = PriceChart()
            chart.addOverlay(overlay)
            chart.candleData = false

            return chart
        }

    init {
        if (condition === Condition.INSIDE && overlay.resultType != BandArray::class)
            throw IllegalArgumentException("condition")
    }

    override fun eval(list: PriceList): Boolean {
        val arr = overlay.eval(list)

        return if (arr is FloatArray) {
            evalFloatArray(arr, list.last)
        } else if (arr is BandArray) {
            evalBandArray(arr)
        } else
            throw UnsupportedOperationException()
    }

    override fun toString(): String {
        return "Price " + condition.toString().toLowerCase() + " " + overlay.toString()
    }

    private fun evalBandArray(arr: BandArray): Boolean {
        val percent = arr.percent(arr.size - 1)

        if (condition === Condition.ABOVE && percent > 1)
            return true
        if (condition === Condition.BELOW && percent < 0)
            return true

        return condition === Condition.INSIDE && percent < 1 && percent > 0

    }

    private fun evalFloatArray(arr: FloatArray, last: Price): Boolean {
        val v = arr.last

        if (condition === Condition.ABOVE && last.close > v)
            return true

        return condition === Condition.BELOW && last.close < v

    }

}
