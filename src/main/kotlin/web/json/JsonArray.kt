package org.cerion.stocklist.web.json

import java.util.ArrayList

class JsonArray(json: String, start: Int) : Json() {

    private val values = ArrayList<Json>()

    init {
        var i = getFirstNonWhitespacePosition(json, start)
        if (json[i] != '[')
            throw RuntimeException()

        i++
        while (i < json.length) {
            val c = json[i]
            if (c == ']') {
                break
            } else if (c == ',') {
                // continue
            } else if (isWhiteSpace(c)) {
                // continue
            } else {
                val value = parse(json, i)
                values.add(value!!)
                i += value.parsedLength - 1
            }

            i++
        }

        parsedLength = i - start + 1
    }

    fun size(): Int = values.size

    operator fun get(index: Int): Json = values[index]

    fun getObject(index: Int): JsonObject = get(index) as JsonObject
}
