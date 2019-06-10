package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class AverageTrueRange extends IndicatorBase {

    public AverageTrueRange() {
        super(Indicator.ATR, 14);
    }

    public AverageTrueRange(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Average True Range";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return averageTrueRange(list, getInt(0) );
    }

    private static FloatArray averageTrueRange(PriceList list, int period)
    {
        FloatArray result = new FloatArray(list.size());

        //Current ATR = [(Prior ATR x 13) + Current TR] / 14
        result.mVal[0] = list.tr(0);
        for(int i = 1; i < list.size(); i++)
            result.mVal[i] = ((result.get(i-1) * (period-1)) + list.tr(i)) / period;

        return result;
    }
}
