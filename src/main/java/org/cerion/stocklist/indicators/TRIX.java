package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class TRIX extends IndicatorBase {

    public TRIX() {
        super(Indicator.TRIX, 15);
    }

    public TRIX(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "TRIX";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return trix(list, getInt(0) );
    }

    protected static FloatArray trix(PriceList list, int period) {
        FloatArray result = new FloatArray(list.size());

        FloatArray ema1 = list.mClose.ema(period);
        FloatArray ema2 = ema1.ema(period);
        FloatArray ema3 = ema2.ema(period);

        for(int i = 1; i < list.size(); i++)
        {
            //1-Day percent change in Triple ExpMovingAverage
            result.mVal[i] = ((ema3.get(i) - ema3.get(i-1)) / ema3.get(i-1)) * 100;
        }

        return result;
    }

}
