package org.cerion.stocklist.web.json;

public abstract class Json {

    protected int parsedLength;

    public static Json parse(String json) {
        return parse(json, 0);
    }

    static Json parse(String json, int start) {
        int i = getFirstNonWhitespacePosition(json, start);

        String debug = json.substring(i);

        char c = json.charAt(i);
        if (c == '[') {
            return new JsonArray(json, i);
        } else if (c == '{') {
            return new JsonObject(json, i);
        } else if (c == '"') {
            return new JsonString(json ,i);
        }

        return null;
    }

    static int getFirstNonWhitespacePosition(String json, int start) {
        int i = start;
        while(i < json.length()) {
            char c = json.charAt(i);
            if (!isWhiteSpace(c))
                break;

            i++;
        }

        return i;
    }

    static boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\r' || c == '\n';
    }
}
