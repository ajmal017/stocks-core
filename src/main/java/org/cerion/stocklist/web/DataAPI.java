package org.cerion.stocklist.web;


import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.model.Dividend;
import org.cerion.stocklist.model.Interval;
import org.cerion.stocklist.model.Quote;
import org.cerion.stocklist.model.Symbol;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataAPI {

    @Deprecated
    PriceList getPrices(String symbol, Interval interval, int max); // TODO possibly replace with date version

    List<Price> getPrices(String symbol, Interval interval, Date start) throws Exception;

    List<Dividend> getDividends(String symbol);

    List<Symbol> getSymbols(Set<String> symbols);
    Symbol getSymbol(String symbol);

    Map<String,Quote> getQuotes(Set<String> symbols);
    Quote getQuote(String symbol);
}
