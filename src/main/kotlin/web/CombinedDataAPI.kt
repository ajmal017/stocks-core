package org.cerion.stocklist.web

import org.cerion.stocklist.Price
import org.cerion.stocklist.model.Dividend
import org.cerion.stocklist.model.Interval
import org.cerion.stocklist.model.Quote
import org.cerion.stocklist.model.Symbol
import org.cerion.stocklist.web.api.GoogleFinance
import org.cerion.stocklist.web.api.Tiingo
import org.cerion.stocklist.web.api.YahooFinance

import java.util.Date

class CombinedDataAPI : DataAPI {

    private val yahoo: YahooFinance = YahooFinance.instance
    private val google: GoogleFinance = GoogleFinance()
    private val tiingo: Tiingo = Tiingo()

    @Throws(Exception::class)
    override fun getPrices(symbol: String, interval: Interval, start: Date): List<Price> {
        return yahoo.getPrices(symbol, interval, start)
    }

    override fun getDividends(symbol: String): List<Dividend> {
        return yahoo.getDividends(symbol)
    }

    override fun getSymbols(symbols: Set<String>): List<Symbol> {
        throw UnsupportedOperationException()
    }

    override fun getSymbol(symbol: String): Symbol? {
        try {
            return tiingo.getSymbol(symbol)
        } catch (ignored: Exception) {

        }

        return null
    }

    override fun getQuotes(symbols: Set<String>): Map<String, Quote> {
        throw UnsupportedOperationException()
    }

    override fun getQuote(symbol: String): Quote {
        return google.getQuote(symbol)!!
    }
}
