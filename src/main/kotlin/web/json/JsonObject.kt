package org.cerion.stocklist.web.json

import java.util.HashMap

class JsonObject internal constructor(json: String, start: Int) : Json() {

    private val values = HashMap<String, Json>()

    init {
        var i = getFirstNonWhitespacePosition(json, start)
        if (json[i] != '{')
            throw RuntimeException()

        i++
        while (i < json.length) {
            val c = json[i]
            if (c == '}') {
                break
            } else if (isWhiteSpace(c)) {
                // continue
            } else if (c == ',') {
                // continue
            } else if (c == '"') {
                var beginIndex = i + 1
                val endIndex = json.indexOf('"', beginIndex)
                val key = json.substring(beginIndex, endIndex)

                beginIndex = endIndex + 1
                beginIndex = json.indexOf(":", beginIndex) + 1

                val value = parse(json, beginIndex)

                i = beginIndex + value!!.parsedLength

                values[key] = value
            }

            i++
        }

        parsedLength = i - start + 1
    }

    operator fun get(key: String): Json? {
        if (values.containsKey(key))
            return values[key]

        return null
    }

    fun getArray(key: String): JsonArray {
        return get(key) as JsonArray
    }

    fun getString(key: String): String {
        return (get(key) as JsonString).value
    }

    fun getFloat(key: String): Float {
        // TODO add parsing for actual number types, not just strings that happen to be numbers
        val str = (get(key) as JsonString).value

        // TODO this should be something else
        return if (str.length > 0)
            java.lang.Float.parseFloat(str)
        else
            java.lang.Float.NaN
    }


}
