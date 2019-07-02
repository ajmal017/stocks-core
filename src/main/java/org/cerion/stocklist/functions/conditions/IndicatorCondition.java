package org.cerion.stocklist.functions.conditions;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.charts.IndicatorChart;
import org.cerion.stocklist.charts.PriceChart;
import org.cerion.stocklist.charts.StockChart;
import org.cerion.stocklist.functions.IFunction;
import org.cerion.stocklist.functions.IIndicator;
import org.cerion.stocklist.functions.IOverlay;

public class IndicatorCondition implements ICondition {

    private IFunction indicator1;
    private IFunction indicator2;
    private Condition condition;
    private float value;

    /**
     * Condition that [indicator1] is [above/below] [indicator2]
     * @param indicator1 indicator to compare from
     * @param condition condition above/below
     * @param indicator2 indicator to compare to
     */
    public IndicatorCondition(IFunction indicator1, Condition condition, IFunction indicator2) {
        // Only valid to compare two float arrays with each other
        if (indicator1.getResultType() != FloatArray.class)
            throw new IllegalArgumentException("indicator1 must return type FloatArray");
        if (indicator2.getResultType() != FloatArray.class)
            throw new IllegalArgumentException("indicator2 must return type FloatArray");

        if (condition == Condition.INSIDE)
            throw new IllegalArgumentException("condition must be above/below");

        this.indicator1 = indicator1;
        this.indicator2 = indicator2;
        this.condition = condition;
    }

    /**
     * Condition that [value] is [above/below/inside] [indicator]
     * @param value Value to check against indicator
     * @param condition condition
     * @param indicator indicator to compare to
     */
    public IndicatorCondition(float value, Condition condition, IFunction indicator) {
        if (condition == Condition.INSIDE && indicator.getResultType() != BandArray.class)
            throw new IllegalArgumentException(Condition.INSIDE + " must be applied to " + BandArray.class);

        this.indicator1 = indicator;
        this.value = value;
        this.condition = condition;
    }

    @Override
    public boolean eval(PriceList list) {

        if (indicator2 != null) {
            FloatArray arr1 = (FloatArray) indicator1.eval(list);
            FloatArray arr2 = (FloatArray) indicator2.eval(list);

            if (condition == Condition.ABOVE)
                return arr1.last() > arr2.last();
            else
                return arr1.last() < arr2.last();
        }

        // Value vs Indicator
        ValueArray va = indicator1.eval(list);
        if (va instanceof BandArray) {
            BandArray bands = (BandArray)va;
            int last = bands.getSize() - 1;
            switch(condition)
            {
                case ABOVE: return value > bands.upper(last);
                case BELOW: return value < bands.lower(last);
                case INSIDE: return value > bands.lower(last) && value < bands.upper(last);
            }
        }

        // FloatArray
        FloatArray arr = (FloatArray) indicator1.eval(list);
        if (condition == Condition.ABOVE)
            return value > arr.last();
        else
            return value < arr.last();
    }

    @Override
    public StockChart getChart() {
        if (indicator1 instanceof IIndicator) {
            IndicatorChart chart = new IndicatorChart((IIndicator)indicator1);
            if (indicator2 != null)
                chart.add((IIndicator)indicator2);

            return chart;
        } else {
            PriceChart chart = new PriceChart();
            chart.addOverlay((IOverlay)indicator1);
            if (indicator2 != null)
                chart.addOverlay((IOverlay)indicator2);

            return chart;
        }
    }

    @Override
    public String toString() {
        if (indicator2 != null)
            return indicator1.toString() + " " + condition.toString().toLowerCase() + " " + indicator2.toString();

        return value + " " + condition.toString().toLowerCase() + " " + indicator1.toString();
    }
}
