package org.cerion.stocks.core.integrationTests

import org.cerion.stocks.core.web.api.TDAmeritrade

class TDAmeritradeTest {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val auth = ""
            val api = TDAmeritrade(auth)

            val ps = api.getPositions()
            ps.forEach { println(it) }
        }
    }
}