package org.cerion.stocklist.web.api

import org.cerion.stocklist.model.Quote
import org.cerion.stocklist.model.Symbol
import org.cerion.stocklist.web.Tools
import org.cerion.stocklist.web.json.Json
import org.cerion.stocklist.web.json.JsonArray
import org.cerion.stocklist.web.json.JsonObject

import java.util.ArrayList

class GoogleFinance {

    fun getQuote(symbol: String): Quote? {
        val url = "https://finance.google.com/finance?q=$symbol&output=json"
        val json = getJson(url) as? JsonArray ?: return null

        // If invalid then result is an array

        val obj = json.getObject(0)

        val quote = Quote(obj.getString("symbol"))
        quote.name = obj.getString("name")
        quote.exchange = obj.getString("exchange")

        quote.lastTrade = obj.getFloat("l")
        quote.change = obj.getFloat("c")
        quote.changePercent = obj.getFloat("cp")

        quote.marketCap = obj.getString("mc")
        quote.peRatio = obj.getFloat("pe")
        quote.dividendYield = obj.getFloat("dy")
        quote.high52 = obj.getFloat("hi52")
        quote.low52 = obj.getFloat("lo52")
        quote.beta = obj.getFloat("beta")
        quote.sector = obj.getString("sname")
        quote.eps = obj.getFloat("eps")

        var volume = obj.getString("vo")
        var mult = 1
        if (volume.contains("M"))
            mult = 1000000
        volume = volume.replace("M", "")
        val v = java.lang.Double.parseDouble(volume) * mult
        quote.volume = v.toLong()

        return quote
    }

    fun getSymbols(symbols: List<String>): List<Symbol>? {
        val tickers = symbols.joinToString(",")
        val url = "https://finance.google.com/finance?q=$tickers&output=json"

        val json = getJson(url) as JsonObject?
        val results = json!!.getArray("searchresults")

        if (results.size() != symbols.size)
            return null

        val result = ArrayList<Symbol>()
        for (i in 0 until results.size()) {
            val entry = results.getObject(i)

            val s = Symbol(entry.getString("ticker"), entry.getString("title"), entry.getString("exchange"))
            result.add(s)
        }

        return null
    }

    private fun getJson(url: String): Json? {
        val data = Tools.getURL(url)

        var i = 0
        while (i < data!!.length) {
            val c = data[i]
            if (c != '\r' && c != '\n' && c != '/')
                break

            i++
        }

        if (data.isNotEmpty())
            return Json.parse(data.substring(i))

        return null
    }
}
