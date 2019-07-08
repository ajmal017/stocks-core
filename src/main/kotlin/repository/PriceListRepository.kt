package org.cerion.stocklist.repository

import org.cerion.stocklist.Price
import org.cerion.stocklist.PriceList
import org.cerion.stocklist.model.HistoricalDates
import org.cerion.stocklist.model.Interval

interface PriceListRepository {
    fun add(list: PriceList)
    fun getHistoricalDates(symbol: String, interval: Interval): HistoricalDates
    fun get(symbol: String, interval: Interval): List<Price>
    fun deleteAll()
    
    @Deprecated("")
    operator fun get(symbol: String, interval: Interval, max: Int): PriceList
}
