package org.cerion.stocklist.functions.conditions


import org.cerion.stocklist.PriceList
import org.cerion.stocklist.charts.StockChart

interface ICondition {
    val chart: StockChart
    fun eval(list: PriceList): Boolean
}
