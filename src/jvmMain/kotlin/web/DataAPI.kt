package org.cerion.stocks.core.web


import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.*
import java.util.*

@Deprecated("Split into smaller interfaces since nothing actually uses all of this")
interface DataAPI {

    fun getPriceList(symbol: String, interval: FetchInterval, start: Date?): PriceList

    fun getPrices(symbol: String, interval: FetchInterval, start: Date): List<PriceRow>

    fun getDividends(symbol: String): List<Dividend>

    fun getSymbol(symbol: String): Symbol?
}
