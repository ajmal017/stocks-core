package org.cerion.stocklist.integrationTests

import org.cerion.stocklist.web.api.TDAmeritrade

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