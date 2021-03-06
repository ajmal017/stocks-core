package org.cerion.stocks.core.repository

import org.cerion.stocks.core.PriceList
import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.Interval
import org.cerion.stocks.core.web.FetchInterval


interface IPriceListRepository {
    fun add(list: PriceList)
    fun get(symbol: String, interval: FetchInterval): PriceList?
}

@Deprecated("use new version and rename when this is removed")
interface PriceListRepository {
    fun add(list: PriceList)

    @Deprecated("use PriceList version")
    fun get(symbol: String, interval: Interval): List<PriceRow>

    fun getList(symbol: String, interval: Interval): PriceList?

    fun deleteAll()
    
    @Deprecated("")
    operator fun get(symbol: String, interval: Interval, max: Int): PriceList
}
