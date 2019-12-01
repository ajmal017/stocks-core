package org.cerion.stocks.core.web

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object Tools {

    fun getUrl(urlString: String, headers: Map<String, String>?): Response {
        val res = Response()

        try {
            //Create connection
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.useCaches = false
            connection.doInput = true

            headers?.forEach { connection.setRequestProperty(it.key, it.value) }
            //connection.setRequestProperty("Content-type", "application/json")
            //connection.setRequestProperty("Authorization", "Token ${Tiingo.API_TOKEN}")

            //Get Response
            res.code = connection.responseCode
            res.headers = connection.headerFields
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
            res.result = response.toString()

        } catch (e: Exception) {
            //println("Response code = $code")
            //e.printStackTrace()
        } finally {
            //connection?.disconnect()
        }

        return res
    }

    fun getURL(theUrl: String): String? {
        var sResult: String? = null

        try {
            val gotoUrl = URL(theUrl)

            try {
                InputStreamReader(gotoUrl.openStream()).use { isr ->
                    BufferedReader(isr).use { input ->
                        val sb = StringBuffer()

                        //grab the contents at the URL
                        for (line in input.lines())
                            sb.append(line + "\r\n")

                        sResult = sb.toString()
                    }
                }
            } catch (e: IOException) {
                sResult = ""
            }

        } catch (mue: MalformedURLException) {
            mue.printStackTrace()
        }

        return sResult
    }


    fun getURL(url: String, cookie: String?): Response {
        val result = Response()

        try {
            val gotoUrl = URL(url)
            val conn = gotoUrl.openConnection()

            if (cookie != null)
                conn.setRequestProperty("Cookie", cookie)

            conn.connect()
            val reader = BufferedReader(InputStreamReader(conn.getInputStream()))

            val sb = StringBuffer()
            //DataInputStream dis = new DataInputStream(conn.getInputStream());

            result.headers = conn.headerFields

            while (true) {
                val line = reader.readLine()
                if (line != null)
                    sb.append(line + "\r\n")
                else
                    break
            }

            result.result = sb.toString()
        } catch (mue: MalformedURLException) {
            mue.printStackTrace()
        } catch (e: IOException) {
            result.result = ""
        }

        return result
    }
}
