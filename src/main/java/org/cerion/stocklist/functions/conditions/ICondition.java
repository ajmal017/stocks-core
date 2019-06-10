package org.cerion.stocklist.functions.conditions;


import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.charts.StockChart;

public interface ICondition {
    boolean eval(PriceList list);
    StockChart getChart();
}
