package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class AverageDirectionalIndex extends IndicatorBase {

    public AverageDirectionalIndex() {
        super(Indicator.ADX, 14);
    }

    public AverageDirectionalIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Average Directional Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return averageDirectionalIndex(list, getInt(0) );
    }

    protected static FloatArray averageDirectionalIndex(PriceList list, int period)
    {
        int size = list.size();
        FloatArray result = new FloatArray(size);

        PairArray di = list.di(period);
        float[] dx = new float[size];

        for(int i = 1; i < size; i++)
        {
            int count = ValueArray.maxPeriod(i, period);
            //Directional Movement Index (DX) equals the absolute value of +DI minus -DI divided by the sum of +DI and -DI.
            float diff = di.getPos(i) - di.getNeg(i);
            float sum = di.getPos(i) + di.getNeg(i);
            dx[i] = 100 * (Math.abs(diff) / sum);

            result.mVal[i] = ((result.get(i-1)*(count-1)) + dx[i]) / count;
        }


        return result;
    }
}
