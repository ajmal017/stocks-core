package org.cerion.stocklist.web.json;

import java.util.HashMap;
import java.util.Map;

public class JsonObject extends Json {

    private Map<String, Json> values = new HashMap<>();

    public JsonObject(String json) {
        this(json, 0);
    }

    JsonObject(String json, int start) {
        int i = getFirstNonWhitespacePosition(json, start);
        if (json.charAt(i) != '{')
            throw new RuntimeException();

        i++;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '}') {
                break;
            } else if (isWhiteSpace(c)) {
                // continue
            } else if (c == ',') {
                // continue
            } else if (c == '"') {
                int beginIndex = i+1;
                int endIndex = json.indexOf('"', beginIndex);
                String key = json.substring(beginIndex, endIndex);

                beginIndex = endIndex + 1;
                beginIndex = json.indexOf(":", beginIndex) + 1;

                Json value = Json.parse(json, beginIndex);

                i = beginIndex + value.parsedLength;

                values.put(key, value);
            }

            i++;
        }

        parsedLength = i - start + 1;
    }

    public Json get(String key) {
        return values.get(key);
    }

    public JsonArray getArray(String key) {
        return (JsonArray)get(key);
    }

    public String getString(String key) {
        return ((JsonString)get(key)).getValue();
    }

    public float getFloat(String key) {
        // TODO add parsing for actual number types, not just strings that happen to be numbers
        String str = ((JsonString)get(key)).getValue();

        // TODO this should be something else
        if (str.length() > 0)
            return Float.parseFloat(str);
        else
            return Float.NaN;
    }


}
