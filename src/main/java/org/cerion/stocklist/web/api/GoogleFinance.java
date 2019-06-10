package org.cerion.stocklist.web.api;

import org.cerion.stocklist.model.Quote;
import org.cerion.stocklist.model.Symbol;
import org.cerion.stocklist.web.Tools;
import org.cerion.stocklist.web.json.Json;
import org.cerion.stocklist.web.json.JsonArray;
import org.cerion.stocklist.web.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class GoogleFinance {

    public Quote getQuote(String symbol) {
        String url = "https://finance.google.com/finance?q=" + symbol + "&output=json";
        Json json = getJson(url);

        // If invalid then result is an array
        if (!(json instanceof JsonArray))
            return null;

        JsonArray arr = (JsonArray)json;
        JsonObject obj = arr.getObject(0);

        Quote quote = new Quote(obj.getString("symbol"));
        quote.name = obj.getString("name");
        quote.exchange = obj.getString("exchange");

        quote.lastTrade = obj.getFloat("l");
        quote.change = obj.getFloat("c");
        quote.changePercent = obj.getFloat("cp");

        quote.marketCap = obj.getString("mc");
        quote.peRatio = obj.getFloat("pe");
        quote.dividendYield = obj.getFloat("dy");
        quote.high52 = obj.getFloat("hi52");
        quote.low52 = obj.getFloat("lo52");
        quote.beta = obj.getFloat("beta");
        quote.sector = obj.getString("sname");
        quote.eps = obj.getFloat("eps");

        String volume = obj.getString("vo");
        int mult = 1;
        if (volume.contains("M"))
            mult = 1000000;
        volume = volume.replace("M","");
        Double v = Double.parseDouble(volume) * mult;
        quote.volume = v.longValue();

        return quote;
    }

    public List<Symbol> getSymbols(List<String> symbols) {
        String tickers = String.join(",", symbols);
        String url = "https://finance.google.com/finance?q=" + tickers + "&output=json";

        JsonObject json = (JsonObject)getJson(url);
        JsonArray results = json.getArray("searchresults");

        if (results.size() != symbols.size())
            return null;

        List<Symbol> result = new ArrayList<>();
        for(int i = 0; i < results.size(); i++) {
            JsonObject entry = results.getObject(i);

            Symbol s = new Symbol(entry.getString("ticker"), entry.getString("title"), entry.getString("exchange"));
            result.add(s);
        }

        return null;
    }

    private Json getJson(String url) {
        String data = Tools.getURL(url);

        int i = 0;
        while (i < data.length()) {
            char c = data.charAt(i);
            if (c != '\r' && c != '\n' && c != '/')
                break;

            i++;
        }

        return Json.parse(data.substring(i));
    }
}
