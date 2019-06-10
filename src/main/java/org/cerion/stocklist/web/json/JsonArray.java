package org.cerion.stocklist.web.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray extends Json {

    private List<Json> values = new ArrayList<>();

    public JsonArray(String json, int start) {
        int i = getFirstNonWhitespacePosition(json, start);
        if (json.charAt(i) != '[')
            throw new RuntimeException();

        i++;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == ']') {
                break;
            } else if (c == ',')
            {
                 // continue
            }
            else if (isWhiteSpace(c)) {
                // continue
            }
            else {
                Json value = Json.parse(json, i);
                values.add(value);
                i += value.parsedLength - 1;
            }

            i++;
        }

        parsedLength = i - start + 1;
    }

    public int size() {
        return values.size();
    }

    public Json get(int index) {
        return values.get(index);
    }

    public JsonObject getObject(int index) {
        return (JsonObject)get(index);
    }
}
