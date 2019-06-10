package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class WilliamsPercentR extends IndicatorBase {

    public WilliamsPercentR() {
        super(Indicator.WPR, 14);
    }

    public WilliamsPercentR(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Williams %R";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return williamsPercentR(list, getInt(0) );
    }

    private static FloatArray williamsPercentR(PriceList list, int period) {
        FloatArray result = new FloatArray(list.size());

        //%R = (Highest High - Close)/(Highest High - Lowest Low) * -100
        for(int i = 0; i < list.size(); i++) {
            float h = list.high(i);
            float l = list.low(i);

            int count = ValueArray.maxPeriod(i,period);
            for(int j = i-count+1; j < i; j++)
            {
                h = Math.max(h, list.high(j));
                l = Math.min(l, list.low(j));
            }

            result.mVal[i] = (h - list.close(i)) / (h - l) * -100;
        }

        return result;
    }
}
