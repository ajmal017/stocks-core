package org.cerion.stocklist.web.json

class JsonString internal constructor(json: String, start: Int) : Json() {

    var value: String
        internal set

    init {
        //val debug = json.substring(start)
        var i = getFirstNonWhitespacePosition(json, start)
        if (json[i] != '"')
            throw JsonException()

        i++
        val beginIndex = i
        while (i < json.length) {
            val c = json[i]

            // TODO handle escaped quotes
            if (c == '"') {
                break
            } else if (isWhiteSpace(c)) {
                // continue
            }

            i++
        }

        value = json.substring(beginIndex, i)
        parsedLength = i - start + 1
    }

    override fun toString(): String {
        return value
    }
}
