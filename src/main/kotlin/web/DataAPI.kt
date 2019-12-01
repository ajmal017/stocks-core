package org.cerion.stocks.core.web


import org.cerion.stocks.core.PriceRow
import org.cerion.stocks.core.model.Dividend
import org.cerion.stocks.core.model.Interval
import org.cerion.stocks.core.model.Quote
import org.cerion.stocks.core.model.Symbol
import java.util.*

interface DataAPI {

    @Throws(Exception::class)
    fun getPrices(symbol: String, interval: Interval, start: Date): List<PriceRow>

    fun getDividends(symbol: String): List<Dividend>

    fun getSymbols(symbols: Set<String>): List<Symbol>
    fun getSymbol(symbol: String): Symbol?

    fun getQuotes(symbols: Set<String>): Map<String, Quote>
    fun getQuote(symbol: String): Quote?
}
