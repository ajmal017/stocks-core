package org.cerion.stocks.core.repository

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.HistoricalDates
import org.cerion.stocks.core.model.Interval

interface PriceListRepository {
    fun add(list: PriceList)

    fun getHistoricalDates(symbol: String, interval: Interval): HistoricalDates?
    fun get(symbol: String, interval: Interval): List<PriceRow>
    fun deleteAll()
    
    @Deprecated("")
    operator fun get(symbol: String, interval: Interval, max: Int): PriceList
}
