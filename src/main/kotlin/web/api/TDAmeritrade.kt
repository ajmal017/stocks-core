package org.cerion.stocks.core.web.api

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.cerion.stocks.core.model.Position
import org.cerion.stocks.core.web.OAuthResponse
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection


private class TDPosition(val data: JSONObject) : Position {
    private val instrument = data["instrument"] as JSONObject
    private val averagePrice = data["averagePrice"] as Double

    override val symbol: String = instrument["symbol"] as String
    override val quantity: Double = data["longQuantity"] as Double
    override val totalValue: Double = data["marketValue"] as Double

    override val cash
        get() = averagePrice == 1.0

    override val pricePerShare: Double
        get() = totalValue / quantity
}

private const val HOST = "https://api.tdameritrade.com"

class TDAmeritradeAuth(private val consumerKey: String) {
    private val client = OkHttpClient()

    fun authorize(code: String): OAuthResponse {
        val postBody: RequestBody = FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("access_type", "offline")
                .add("client_id", consumerKey)
                .add("code", code)
                .add("redirect_uri", "https://127.0.0.1")
                .build()

        val request = Request.Builder()
                .url("https://api.tdameritrade.com/v1/oauth2/token")
                .post(postBody)
                .build()

        val response = client.newCall(request).execute()
        if (response.code != HttpURLConnection.HTTP_OK)
            throw RequestException(response)

        val body = response.body?.string()
        println(body)

        val json = JSONObject(body)
        return OAuthResponse(
                json["access_token"] as String,
                json["refresh_token"] as String,
                json["expires_in"] as Int,
                json["refresh_token_expires_in"] as Int)
    }

    fun refreshAuth(refreshToken: String): OAuthResponse {
        val postBody: RequestBody = FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("client_id", consumerKey)
                .add("refresh_token", refreshToken)
                .build()

        val request = Request.Builder()
                .url("https://api.tdameritrade.com/v1/oauth2/token")
                .post(postBody)
                .build()

        val response = client.newCall(request).execute()
        if (response.code != HttpURLConnection.HTTP_OK)
            throw RequestException(response)

        val body = response.body?.string()
        println(body)

        val json = JSONObject(body)
        return OAuthResponse(
                json["access_token"] as String,
                null,
                json["expires_in"] as Int,
                null)
    }
}

class TDAmeritrade(private val auth: String) {
    private val client = OkHttpClient()

    fun getPositions(): List<Position> {
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
            val position = TDPosition(p)
            result.add(position)
        }

        return result
    }
}