package org.cerion.stocklist.web.json

abstract class Json {

    var parsedLength: Int = 0

    companion object {

        fun parse(json: String): Json? {
            return parse(json, 0)
        }

        internal fun parse(json: String, start: Int): Json? {
            val i = getFirstNonWhitespacePosition(json, start)

            //val debug = json.substring(i)

            val c = json[i]
            if (c == '[') {
                return JsonArray(json, i)
            } else if (c == '{') {
                return JsonObject(json, i)
            } else if (c == '"') {
                return JsonString(json, i)
            }

            return null
        }

        internal fun getFirstNonWhitespacePosition(json: String, start: Int): Int {
            var i = start
            while (i < json.length) {
                val c = json[i]
                if (!isWhiteSpace(c))
                    break

                i++
            }

            return i
        }

        internal fun isWhiteSpace(c: Char): Boolean {
            return c == ' ' || c == '\r' || c == '\n'
        }
    }
}
