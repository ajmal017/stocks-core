package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class MoneyFlowIndex extends IndicatorBase {

    public MoneyFlowIndex() {
        super(Indicator.MFI, 14);
    }

    public MoneyFlowIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Money Flow Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return moneyFlowIndex(list, getInt(0) );
    }

    private static FloatArray moneyFlowIndex(PriceList list, int period) {
        FloatArray result = new FloatArray(list.size());

        //Typical Price = (High + Low + Close)/3
        //Raw Money Flow = Typical Price x Volume
        //Money Flow Ratio = (14-period Positive Money Flow)/(14-period Negative Money Flow)
        //Money Flow Index = 100 - 100/(1 + Money Flow Ratio)
        for(int i = period; i < list.size(); i++)
        {
            float posflow = 0;
            float negflow = 0;
            for(int j = i-period+1; j <= i; j++)
            {
                if(list.tp(j) > list.tp(j-1))
                    posflow += list.tp(j) * list.volume(j);
                else
                    negflow += list.tp(j) * list.volume(j);
            }

            float ratio = posflow / negflow;
            result.mVal[i] = 100 - (100/(1+ratio));
        }

        return result;
    }
}
