package org.cerion.stocklist.repository

import org.cerion.stocklist.model.Dividend
import org.cerion.stocklist.model.HistoricalDates

interface DividendRepository {
    fun get(symbol: String): List<Dividend>
    fun getHistoricalDates(symbol: String): HistoricalDates?
    fun add(symbol: String, list: List<Dividend>)
    fun deleteAll()
}
