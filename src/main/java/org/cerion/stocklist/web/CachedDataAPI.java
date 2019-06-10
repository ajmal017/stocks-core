package org.cerion.stocklist.web;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.model.Interval;

import java.util.Date;
import java.util.List;

public interface CachedDataAPI extends DataAPI {
    void clearCache();
    List<Price> getPrices(String symbol, Interval interval, Date start, boolean forceUpdate) throws Exception;
}
