package org.cerion.stocklist.web

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

object Tools {

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

            for (line in reader.lines()) {
                if (sb.isNotEmpty())
                    sb.append("\r\n" + line)
                else
                    sb.append(line)
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
