package org.cerion.stocks.core.web

import org.cerion.stocks.core.PriceRow
import java.util.*

// Intervals the APIs allow fetching for
enum class FetchInterval {
    DAILY,
    WEEKLY,
    MONTHLY,
}

interface PriceHistoryDataSource {
    fun getPrices(symbol: String, interval: FetchInterval, start: Date?): List<PriceRow>
}