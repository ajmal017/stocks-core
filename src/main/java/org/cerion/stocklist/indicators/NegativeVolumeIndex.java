package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class NegativeVolumeIndex extends IndicatorBase {

    public NegativeVolumeIndex() {
        super(Indicator.NVI);
    }

    @Override
    public String getName() {
        return "Negative Volume Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return negativeVolumeIndex(list );
    }

    private static FloatArray negativeVolumeIndex(PriceList list)
    {
        FloatArray result = new FloatArray(list.size());

        result.mVal[0] = 1000;
        for(int i = 1; i < list.size(); i++)
        {
            if(list.volume(i) < list.volume(i-1))
                result.mVal[i] = result.mVal[i-1] + list.roc(i, 1);
            else
                result.mVal[i] = result.mVal[i-1];

        }

        return result;
    }
}
