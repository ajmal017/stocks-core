package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class OnBalanceVolume extends IndicatorBase {

    public OnBalanceVolume() {
        super(Indicator.OBV);
    }

    @Override
    public String getName() {
        return "On Balance Volume";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return onBalanceVolume(list );
    }

    private static FloatArray onBalanceVolume(PriceList list) {
        FloatArray close = list.mClose;
        FloatArray volume = list.mVolume;
        FloatArray result = new FloatArray(list.size());

        result.mVal[0] = 0;
        for(int i = 1; i < list.size(); i++)
        {
            if(close.get(i) > close.get(i-1))
                result.mVal[i] = result.get(i-1) + volume.get(i);
            else if(close.get(i) < close.get(i-1))
                result.mVal[i] = result.get(i-1) - volume.get(i);
            else
                result.mVal[i] = result.get(i-1);
        }

        return result;
    }
}
