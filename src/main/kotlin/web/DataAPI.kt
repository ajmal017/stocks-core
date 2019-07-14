package org.cerion.stocklist.web


import org.cerion.stocklist.Price
import org.cerion.stocklist.model.Dividend
import org.cerion.stocklist.model.Interval
import org.cerion.stocklist.model.Quote
import org.cerion.stocklist.model.Symbol
import java.util.*

interface DataAPI {

    @Throws(Exception::class)
    fun getPrices(symbol: String, interval: Interval, start: Date): List<Price>

    fun getDividends(symbol: String): List<Dividend>

    fun getSymbols(symbols: Set<String>): List<Symbol>
    fun getSymbol(symbol: String): Symbol?

    fun getQuotes(symbols: Set<String>): Map<String, Quote>
    fun getQuote(symbol: String): Quote?
}
