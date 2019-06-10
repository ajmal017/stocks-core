package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class Stochastic extends IndicatorBase {

    public Stochastic() {
        super(Indicator.STOCH, 14, 3, 3);
    }

    // TODO allow 1-3 parameters
    public Stochastic(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Stochastic Oscillator";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return stochastic(list, getInt(0), getInt(1), getInt(2) );
    }

    private static FloatArray stochastic(PriceList list, int K) {
        FloatArray result = new FloatArray(list.size());
        FloatArray highs = list.getHigh();
        FloatArray lows = list.getLow();

        //K = period
        for(int i = 0; i < list.size(); i++) {
            int period = ValueArray.maxPeriod(i, K);

            float high = highs.max(i-period+1, i);
            float low = lows.min(i-period+1, i);

            //K = (Current Close - Lowest Low)/(Highest High - Lowest Low) * 100
            result.mVal[i] = ((list.close(i) - low)/(high-low))*100;
        }

        return result;
    }

    private static FloatArray stochastic(PriceList list, int K, int D) {
        FloatArray result = stochastic(list,K);
        return result.sma(D);
    }

    private static FloatArray stochastic(PriceList list, int K, int fastD, int slowD) {
        FloatArray result = stochastic(list,K,fastD);
        return result.sma(slowD);
    }

}
