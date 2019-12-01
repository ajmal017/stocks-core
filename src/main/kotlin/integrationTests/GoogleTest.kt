package org.cerion.stocks.core.integrationTests

import org.cerion.stocks.core.web.api.GoogleFinance

class GoogleTest {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val api = GoogleFinance()

            var q = api.getQuote("OHI")
            if (!q!!.exchange!!.contentEquals("NYSE"))
                throw Exception("invalid exchange")
            if (!q.name!!.contentEquals("Omega Healthcare Investors Inc"))
                throw Exception("invalid name")

            q = api.getQuote("ASDF")
            if (q != null)
                throw Exception("invalid should be null")

            q = api.getQuote("AAPL")
            if (q!!.volume < 1000000)
                throw Exception("Volume")
            if (q.beta <= 0)
                throw Exception("Beta")
        }
    }
}