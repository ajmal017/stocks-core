package org.cerion.stocks.core.repository

import org.cerion.stocks.core.model.Dividend

interface DividendRepository {
    fun get(symbol: String): List<Dividend>
    fun add(symbol: String, list: List<Dividend>)
    fun deleteAll()
}
