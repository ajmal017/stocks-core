package org.cerion.stocklist.repository;

import org.cerion.stocklist.model.Dividend;
import org.cerion.stocklist.model.HistoricalDates;

import java.util.List;

public interface DividendRepository {
    List<Dividend> get(String symbol);
    HistoricalDates getHistoricalDates(String symbol);
    void add(String symbol, List<Dividend> list);
    void deleteAll();
}
