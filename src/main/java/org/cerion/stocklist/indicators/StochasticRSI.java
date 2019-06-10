package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class StochasticRSI extends IndicatorBase {

    public StochasticRSI() {
        super(Indicator.STOCHRSI, 14);
    }

    public StochasticRSI(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Stochastic RSI";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return stochasticRSI(list, getInt(0) );
    }

    private static FloatArray stochasticRSI(PriceList list, int period) {
        FloatArray result = new FloatArray(list.size());

        FloatArray rsi_arr = list.rsi(period);

        for(int i = 0; i < list.size(); i++)
        {
            float high = rsi_arr.get(i);
            float low = rsi_arr.get(i);

            int count = ValueArray.maxPeriod(i, period);
            for(int j = i-count+1; j < i; j++)
            {
                float rsi = rsi_arr.get(j);
                if(rsi > high)
                    high = rsi;
                if(rsi < low)
                    low = rsi;
            }

            //StochRSI = (RSI - Lowest Low RSI) / (Highest High RSI - Lowest Low RSI)
            if(high == low)
                result.mVal[i] = 1;
            else
                result.mVal[i] = (rsi_arr.get(i) - low) / (high - low);
        }

        return result;
    }

}
