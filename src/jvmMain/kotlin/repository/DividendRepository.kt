package org.cerion.stocks.core.repository

import org.cerion.stocks.core.model.Dividend
import org.cerion.stocks.core.model.HistoricalDates

interface DividendRepository {
    fun get(symbol: String): List<Dividend>
    fun getHistoricalDates(symbol: String): HistoricalDates?
    fun add(symbol: String, list: List<Dividend>)
    fun deleteAll()
}
