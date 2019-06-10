package org.cerion.stocklist.web.json;

public class JsonString extends Json {

    String value;

    JsonString(String json, int start) {
        String debug = json.substring(start);
        int i = getFirstNonWhitespacePosition(json, start);
        if (json.charAt(i) != '"')
            throw new RuntimeException();

        i++;
        int beginIndex = i;
        while (i < json.length()) {
            char c = json.charAt(i);

            // TODO handle escaped quotes

            if (c == '"') {
                break;
            } else if (isWhiteSpace(c)) {
                // continue
            }

            i++;
        }

        value = json.substring(beginIndex, i);
        parsedLength = i - start + 1;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
