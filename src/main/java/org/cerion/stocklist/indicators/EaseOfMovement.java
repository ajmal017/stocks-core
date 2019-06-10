package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class EaseOfMovement extends IndicatorBase {

    public EaseOfMovement() {
        super(Indicator.EMV, 14);
    }

    public EaseOfMovement(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Ease of Movement";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return easeOfMovement(list, getInt(0) );
    }

    //1-period EMV
    private static FloatArray easeOfMovement(PriceList list)
    {
        FloatArray result = new FloatArray(list.size());

        //Distance Moved = ((H + L)/2 - (Prior H + Prior L)/2)
        //Box Ratio = ((V/100,000,000)/(H - L))
        //1-Period EMV = dm / box
        for(int i = 1; i < list.size(); i++)
        {
            float diff = list.high(i) - list.low(i); //Need to divide by this
            if(diff == 0)
                diff = 0.01f;

            float dm = ((list.high(i) + list.low(i))/2 - (list.high(i-1) + list.low(i-1))/2);
            float box = ((list.volume(i)/100000.0f)/diff); //Volume is already divided by 1000 so removing 2 digits here
            result.mVal[i] = dm / box;
            if(box == 0)
                result.mVal[i] = 0;
        }

        return result;
    }

    //TODO, double check results again with current arrays
    private static FloatArray easeOfMovement(PriceList list, int period)
    {
        FloatArray result = new FloatArray(list.size());

        //N-Period Ease of Movement = N-Period simple moving average of 1-period EMV
        FloatArray emv = easeOfMovement(list);

        for(int i = 1; i < list.size(); i++)
        {
            int count = ValueArray.maxPeriod(i, period);
            float total = 0;
            for(int j = i-count+1; j <= i; j++)
                total += emv.get(j);

            result.mVal[i] = total / count;
        }

        return result;
    }
}
