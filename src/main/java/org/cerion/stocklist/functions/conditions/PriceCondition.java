package org.cerion.stocklist.functions.conditions;

import org.cerion.stocklist.Price;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.charts.PriceChart;
import org.cerion.stocklist.charts.StockChart;
import org.cerion.stocklist.functions.IPriceOverlay;

public class PriceCondition implements ICondition {

    private Condition condition;
    private IPriceOverlay overlay;

    public PriceCondition(Condition condition, IPriceOverlay overlay) {
        if (condition == Condition.INSIDE && overlay.getResultType() != BandArray.class)
            throw new IllegalArgumentException("condition");

        this.condition = condition;
        this.overlay = overlay;
    }

    public boolean eval(PriceList list) {
        ValueArray arr = overlay.eval(list);

        if (arr instanceof FloatArray) {
            return evalFloatArray((FloatArray)arr, list.getLast());
        } else if (arr instanceof BandArray) {
            return evalBandArray((BandArray)arr);
        } else
            throw new UnsupportedOperationException();
    }

    @Override
    public StockChart getChart() {
        PriceChart chart = new PriceChart();
        chart.addOverlay(overlay);
        chart.candleData = false;

        return chart;
    }

    @Override
    public String toString() {
        return "Price " + condition.toString().toLowerCase() + " " + overlay.toString();
    }

    private boolean evalBandArray(BandArray arr) {
        float percent = arr.percent(arr.size()-1);

        if (condition == Condition.ABOVE && percent > 1)
            return true;
        if (condition == Condition.BELOW && percent < 0)
            return true;
        if (condition == Condition.INSIDE && percent < 1 && percent > 0)
            return true;

        return false;
    }

    private boolean evalFloatArray(FloatArray arr, Price last) {
        float v = arr.last();

        if (condition == Condition.ABOVE && last.close > v)
            return true;
        if (condition == Condition.BELOW && last.close < v)
            return true;

        return false;
    }

}
