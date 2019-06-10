package org.cerion.stocklist.repository;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.model.HistoricalDates;
import org.cerion.stocklist.model.Interval;

import java.util.List;

public interface PriceListRepository {

    void add(PriceList list);
    HistoricalDates getHistoricalDates(String symbol, Interval interval);

    @Deprecated
    PriceList get(String symbol, Interval interval, int max);

    List<Price> get(String symbol, Interval interval);
    void deleteAll();
}
