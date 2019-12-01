package org.cerion.stocks.core.web

import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.Interval
import java.util.*

interface CachedDataAPI : DataAPI {
    fun clearCache()

    @Throws(Exception::class)
    fun getPrices(symbol: String, interval: Interval, start: Date, forceUpdate: Boolean): List<PriceRow>
}
