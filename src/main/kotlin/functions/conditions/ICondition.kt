package org.cerion.stocks.core.functions.conditions


import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.charts.StockChart

interface ICondition {
    val chart: StockChart
    fun eval(list: PriceList): Boolean
}
