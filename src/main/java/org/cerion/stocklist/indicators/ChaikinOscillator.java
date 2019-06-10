package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class ChaikinOscillator extends IndicatorBase {

    public ChaikinOscillator() {
        super(Indicator.CO, 3, 10);
    }

    public ChaikinOscillator(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Chaikin Oscillator";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return chaikinOscillator(list, getInt(0), getInt(1) );
    }

    private static FloatArray chaikinOscillator(PriceList list, int p1, int p2) {
        FloatArray result = new FloatArray(list.size());

        FloatArray adl = list.adl();
        FloatArray ema1 = adl.ema(p1);
        FloatArray ema2 = adl.ema(p2);

        for(int i = 0; i < list.size(); i++)
            result.mVal[i] = ema1.get(i) - ema2.get(i);

        return result;
    }

}
