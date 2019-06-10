package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class ForceIndex extends IndicatorBase {

    public ForceIndex() {
        super(Indicator.FORCE_INDEX, 13);
    }

    public ForceIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Force Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return forceIndex(list, getInt(0) );
    }

    private static FloatArray forceIndex(PriceList list, int period)
    {
        FloatArray close = list.mClose;
        int size = list.size();
        FloatArray result = new FloatArray(size);

        float mult = 2.0f / (1f + period);

        for(int i = 1; i < size; i++)
        {
            //Price p = get(i);
            //Price prev = get(i-1);

            float fi = (close.get(i) - close.get(i-1)) * list.mVolume.get(i);
            result.mVal[i] = (fi - result.get(i-1)) * mult + result.get(i-1);
            //System.out.println(p.date + "\t" + p.fi);
        }

        return result;
    }
}
