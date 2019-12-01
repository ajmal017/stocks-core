package org.cerion.stocks.core.web.api

import org.cerion.stocks.core.model.Symbol
import org.cerion.stocks.core.web.json.Json
import org.cerion.stocks.core.web.json.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Tiingo {

    fun getSymbol(symbol: String): Symbol? {
        val url = "https://api.tiingo.com/tiingo/daily/$symbol"
        val data = getData(url)
        val json = Json.parse(data!!) as JsonObject

        return Symbol(symbol, json["name"].toString(), json["exchangeCode"].toString())
    }

    companion object {
        private const val API_TOKEN = "e113430723313f765f12a4d130acd1c5aac78eee"

        private fun getData(targetURL: String): String? {

            val url: URL
            var code = 0

            try {
                //Create connection
                url = URL(targetURL)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.useCaches = false
                connection.doInput = true

                connection.setRequestProperty("Content-type", "application/json")
                connection.setRequestProperty("Authorization", "Token $API_TOKEN")

                //Get Response
                code = connection.responseCode
                val stream = connection.inputStream
                val rd = BufferedReader(InputStreamReader(stream))
                var line: String?
                val response = StringBuffer()
                while (true) {
                    line = rd.readLine()
                    if (line == null)
                        break

                    response.append(line)
                    response.append('\r')
                }
                rd.close()
                return response.toString()

            } catch (e: Exception) {
                println("Response code = $code")
                //e.printStackTrace()
                return null

            } finally {

                //connection?.disconnect()
            }
        }
    }

}
