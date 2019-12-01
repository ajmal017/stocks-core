package org.cerion.stocks.core.web.api

import org.cerion.stocks.core.web.Tools
import org.json.JSONArray
import org.json.JSONObject


data class TDPosition(val symbol: String, val longQuantity: Double, val marketValue: Double)


class TDAmeritrade(private val auth: String) {

    fun getPositions(): List<TDPosition> {

        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "Bearer $auth"

        // TODO replace with okhttp
        val jsonText = Tools.getUrl("$HOST/v1/accounts?fields=positions", headers)

        val accounts = JSONArray(jsonText.result)[0] as JSONObject
        val securitiesAccount = accounts["securitiesAccount"] as JSONObject
        val positions = securitiesAccount["positions"] as JSONArray

        val result = mutableListOf<TDPosition>()
        positions.forEach {
            val p = it as JSONObject
            val position = TDPosition(
                    (p["instrument"] as JSONObject)["symbol"] as String,
                    p["longQuantity"] as Double,
                    p["marketValue"] as Double)
            result.add(position)
        }

        return result
    }


    companion object {
        const val HOST = "https://api.tdameritrade.com"
    }

}