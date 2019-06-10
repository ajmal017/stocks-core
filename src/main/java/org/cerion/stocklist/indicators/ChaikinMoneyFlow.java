package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class ChaikinMoneyFlow extends IndicatorBase {

    public ChaikinMoneyFlow() {
        super(Indicator.CMF, 20);
    }

    public ChaikinMoneyFlow(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Chaikin Money Flow";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return chaikinMoneyFlow(list, getInt(0) );
    }

    private static FloatArray chaikinMoneyFlow(PriceList list, int period) {
        FloatArray result = new FloatArray(list.size());

        //CMF = N-period Sum of Money Flow Volume / N period Sum of Volume
        for(int i = 0; i < list.size(); i++)
        {
            int start = i - ValueArray.maxPeriod(i, period) + 1;
            float mfvolume = 0;
            float volume = 0;
            for(int j = start; j <= i; j++)
            {
                mfvolume += list.mfv(j);
                volume += list.volume(j);
            }

            result.mVal[i] = mfvolume / volume;
        }

        return result;
    }
}
