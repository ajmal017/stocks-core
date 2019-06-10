package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class MassIndex extends IndicatorBase {

    public MassIndex() {
        super(Indicator.MASS_INDEX, 25);
    }

    public MassIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Mass Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return massIndex(list, getInt(0) );
    }

    private static FloatArray massIndex(PriceList list, int period) {
        int size = list.size();
        FloatArray result = new FloatArray(size);

        //Single ExpMovingAverage = 9-period exponential moving average (ExpMovingAverage) of the high-low differential
        //Double ExpMovingAverage = 9-period ExpMovingAverage of the 9-period ExpMovingAverage of the high-low differential
        //ExpMovingAverage Ratio = Single ExpMovingAverage divided by Double ExpMovingAverage
        //Mass Index = 25-period sum of the ExpMovingAverage Ratio

        FloatArray highLowDiff = new FloatArray(size);
        for(int i = 0; i < size; i++) {
            highLowDiff.mVal[i] = list.high(i) - list.low(i);
        }

        FloatArray ema = highLowDiff.ema(9);
        FloatArray ema2 = ema.ema(9);
        FloatArray emaRatio = new FloatArray(size);

        //X period sum
        for(int i = 0; i < size; i++) {
            emaRatio.mVal[i] = ema.get(i) / ema2.get(i);

            //int max = ValueArray.maxPeriod(i, period);
            if(i >= period-1) {
                result.mVal[i] = emaRatio.sum(i-period+1, i);
            } else if(i == 0) {
                result.mVal[i] = period;
            } else {
                // Normalize, average ema ratio is 1
                // X period sum of ratio is average of the period size
                // Anything value before the period size will be normalized so the average stays the same
                float mult = period / (1.0f + i);
                result.mVal[i] = mult * emaRatio.sum(0, i);
            }
        }

        return result;
    }
}
