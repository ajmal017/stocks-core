package org.cerion.stocks.core.web.api

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection

data class TDPosition(val symbol: String, val longQuantity: Double, val marketValue: Double)

private const val HOST = "https://api.tdameritrade.com"

class TDAmeritrade(private val auth: String) {
    private val client = OkHttpClient()

    fun getPositions(): List<TDPosition> {
        val request = Request.Builder()
                .header("Authorization", "Bearer $auth")
                .url("$HOST/v1/accounts?fields=positions")
                .build()

        val response = client.newCall(request).execute()
        if (response.code != HttpURLConnection.HTTP_OK)
            throw RequestException(response)

        val body = response.body?.string()
        val accounts = JSONArray(body)[0] as JSONObject
        val securitiesAccount = accounts["securitiesAccount"] as JSONObject
        val positions = securitiesAccount["positions"] as JSONArray

        val result = mutableListOf<TDPosition>()

        for(i in 0 until positions.length()) {
            val p = positions[i] as JSONObject
            val position = TDPosition(
                    (p["instrument"] as JSONObject)["symbol"] as String,
                    p["longQuantity"] as Double,
                    p["marketValue"] as Double)
            result.add(position)
        }

        return result
    }
}