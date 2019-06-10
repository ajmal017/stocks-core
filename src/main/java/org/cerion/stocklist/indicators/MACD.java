package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.MACDArray;
import org.cerion.stocklist.functions.types.Indicator;

public class MACD extends IndicatorBase {

    public MACD() {
        super(Indicator.MACD, 12, 26, 9);
    }

    public MACD(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "MACD";
    }

    @Override
    public MACDArray eval(PriceList list) {
        return macd(list, getInt(0), getInt(1), getInt(2) );
    }

    private static MACDArray macd(PriceList list, int p1, int p2, int signal) {
        MACDArray result = new MACDArray(list.size());

        FloatArray ema1 = list.mClose.ema(p1);
        FloatArray ema2 = list.mClose.ema(p2);

        for(int i = 0; i < list.size(); i++)
            result.mVal[i] = ema1.get(i) - ema2.get(i);

        result.setSignal(signal);
        return result;
    }
}
