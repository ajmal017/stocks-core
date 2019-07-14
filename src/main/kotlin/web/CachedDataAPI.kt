package org.cerion.stocklist.web

import org.cerion.stocklist.Price
import org.cerion.stocklist.model.Interval

import java.util.Date

interface CachedDataAPI : DataAPI {
    fun clearCache()

    @Throws(Exception::class)
    fun getPrices(symbol: String, interval: Interval, start: Date, forceUpdate: Boolean): List<Price>
}
