package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class UlcerIndex extends IndicatorBase {

    public UlcerIndex() {
        super(Indicator.ULCER_INDEX, 14);
    }

    public UlcerIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Ulcer Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return eval(list.getClose());
    }

    public FloatArray eval(FloatArray arr) {
        int period = getInt(0);
        int size = arr.size();
        FloatArray result = new FloatArray(size);

        //Percent-Drawdown = ((Close - 14-period Max Close)/14-period Max Close) x 100
        //Squared Average = (14-perod Sum of Percent-Drawdown Squared)/14
        //Ulcer Index = Square Root of Squared Average

        //Set Percent Drawdown
        float[] percentD = new float[size];
        for(int i = 0; i < size; i++)
        {
            float max = 0; //Max close
            int count = ValueArray.maxPeriod(i, period);
            for(int j = i-count+1; j <= i; j++)
                max = Math.max(max, arr.get(j));

            percentD[i] = ((arr.get(i) - max)/max) * 100;
        }

        for(int i = 0; i < size; i++)
        {
            float avg = 0;
            int count = ValueArray.maxPeriod(i, period);
            for(int j = i-count+1; j <= i; j++)
                avg += percentD[j] * percentD[j]; //Sum of squared

            avg /= period;
            result.mVal[i] = (float) Math.sqrt(avg);
        }

        return result;
    }
}
