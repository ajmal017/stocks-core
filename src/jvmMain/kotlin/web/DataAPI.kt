package org.cerion.stocks.core.web


import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.*
import java.util.*

// TODO split this into multiple interfaces since nothing implements everything directly
interface DataAPI {

    fun getPriceList(symbol: String, interval: FetchInterval, start: Date?): PriceList

    @Deprecated("Use pricelist version")
    fun getPrices(symbol: String, interval: FetchInterval, start: Date): List<PriceRow>

    fun getDividends(symbol: String): List<Dividend>

    fun getSymbol(symbol: String): Symbol?
}
