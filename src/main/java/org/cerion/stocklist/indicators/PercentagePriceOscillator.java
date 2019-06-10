package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.MACDArray;
import org.cerion.stocklist.functions.types.Indicator;

public class PercentagePriceOscillator extends IndicatorBase {

    public PercentagePriceOscillator() {
        super(Indicator.PPO, 12, 26, 9);
    }

    public PercentagePriceOscillator(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Percentage Price Oscillator";
    }

    @Override
    public MACDArray eval(PriceList list) {
        //Percentage version of MACD
        return getPercentMACD(list.mClose, getInt(0), getInt(1), getInt(2) );
    }

    // Shared with PVO
    protected static MACDArray getPercentMACD(FloatArray arr, int p1, int p2, int signal) {
        MACDArray result = new MACDArray(arr.size());
        FloatArray ema1 = arr.ema(p1);
        FloatArray ema2 = arr.ema(p2);

        for(int i = 0; i < arr.size(); i++)
            result.mVal[i] = 100 * (ema1.get(i) - ema2.get(i)) / ema2.get(i);

        result.setSignal(signal);
        return result;
    }

}
