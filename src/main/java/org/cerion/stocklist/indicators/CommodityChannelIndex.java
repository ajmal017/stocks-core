package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class CommodityChannelIndex extends IndicatorBase {

    public CommodityChannelIndex() {
        super(Indicator.CCI, 20);
    }

    public CommodityChannelIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Commodity Channel Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return commodityChannelIndex(list, getInt(0) );
    }

    private static FloatArray commodityChannelIndex(PriceList list, int period) {
        int size = list.size();
        FloatArray result = new FloatArray(size);

        FloatArray tp = new FloatArray(size);
        for(int i = 0; i < size; i++)
            tp.mVal[i] = list.tp(i);

        FloatArray sma_arr = tp.sma(period);

        for(int i = 1; i < size; i++)
        {
            float sma = sma_arr.get(i);
            int count = ValueArray.maxPeriod(i, period);

            //Mean deviation is different than standard deviation
            float dev = 0;
            for(int j = i-count+1; j <= i; j++)
                dev += Math.abs( list.get(j).tp() - sma);
            dev = dev / count;

            //CCI = (Typical Price  -  20-period SimpleMovingAverage of TP) / (.015 x Mean Deviation)
            result.mVal[i] = (list.tp(i) - sma) / (0.015f * dev);
        }

        return result;
    }
}
