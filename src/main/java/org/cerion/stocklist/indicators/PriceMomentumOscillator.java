package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class PriceMomentumOscillator extends IndicatorBase {

    public PriceMomentumOscillator() {
        super(Indicator.PMO, 35, 20);
    }

    public PriceMomentumOscillator(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Price Momentum Oscillator";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return priceMomentumOscillator(list, getInt(0), getInt(1) );
    }

    private static FloatArray priceMomentumOscillator(PriceList list, int p1, int p2) {
        FloatArray result = new FloatArray(list.size());

        float m1 = 2.0f/p1;
        float m2 = 2.0f/p2;
        float ema = 0;

        for(int i = 1; i < list.size(); i++)
        {
            float roc = list.roc(i,1);

            ema = (roc*m1)+(ema*(1-m1));

            float e = ema * 10;
            result.mVal[i] = ((e - result.get(i-1)) *m2 ) + result.get(i-1);
        }

        return result;
    }

}
