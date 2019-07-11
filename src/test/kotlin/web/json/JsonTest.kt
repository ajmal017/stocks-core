package org.cerion.stocklist.web.json

import org.cerion.stocklist.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonTest {

    @Test
    fun json_basic() {
        val data = Utils.resourceToString("tiingo_daily.json")
        val json = Json.parse(data) as JsonObject

        assertEquals("XLE", json.getString("ticker"))
    }
}