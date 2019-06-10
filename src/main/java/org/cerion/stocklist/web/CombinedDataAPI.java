package org.cerion.stocklist.web;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.model.Dividend;
import org.cerion.stocklist.model.Interval;
import org.cerion.stocklist.model.Quote;
import org.cerion.stocklist.model.Symbol;
import org.cerion.stocklist.web.api.GoogleFinance;
import org.cerion.stocklist.web.api.YahooFinance;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CombinedDataAPI implements DataAPI {

    private YahooFinance yahoo;
    private GoogleFinance google;

    public CombinedDataAPI() {
        yahoo = YahooFinance.getInstance();
        google = new GoogleFinance();
    }

    @Override
    public PriceList getPrices(String symbol, Interval interval, int max) {
        return yahoo.getPrices(symbol, interval, max);
    }

    @Override
    public List<Price> getPrices(String symbol, Interval interval, Date start) throws Exception {
        return yahoo.getPrices(symbol, interval, start);
    }

    @Override
    public List<Dividend> getDividends(String symbol) {
        return yahoo.getDividends(symbol);
    }

    @Override
    public List<Symbol> getSymbols(Set<String> symbols) {
        throw new NotImplementedException();
    }

    @Override
    public Symbol getSymbol(String symbol) {
        Quote q = google.getQuote(symbol);
        return new Symbol(q.symbol, q.name, q.exchange);
    }

    @Override
    public Map<String, Quote> getQuotes(Set<String> symbols) {
        throw new NotImplementedException();
    }

    @Override
    public Quote getQuote(String symbol) {
        return google.getQuote(symbol);
    }
}
