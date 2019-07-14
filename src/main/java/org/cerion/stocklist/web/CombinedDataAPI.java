package org.cerion.stocklist.web;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.model.Dividend;
import org.cerion.stocklist.model.Interval;
import org.cerion.stocklist.model.Quote;
import org.cerion.stocklist.model.Symbol;
import org.cerion.stocklist.web.api.GoogleFinance;
import org.cerion.stocklist.web.api.Tiingo;
import org.cerion.stocklist.web.api.YahooFinance;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CombinedDataAPI implements DataAPI {

    private YahooFinance yahoo;
    private GoogleFinance google;
    private Tiingo tiingo;

    public CombinedDataAPI() {
        yahoo = YahooFinance.Companion.getInstance();
        google = new GoogleFinance();
        tiingo = new Tiingo();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Symbol getSymbol(String symbol) {
        try {
            return tiingo.getSymbol(symbol);
        }
        catch (Exception ignored) {

        }

        return null;
    }

    @Override
    public Map<String, Quote> getQuotes(Set<String> symbols) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Quote getQuote(String symbol) {
        return google.getQuote(symbol);
    }
}
