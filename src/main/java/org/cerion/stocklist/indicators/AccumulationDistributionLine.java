package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class AccumulationDistributionLine extends IndicatorBase {

    public AccumulationDistributionLine() {
        super(Indicator.ADL);
    }

    @Override
    public String getName() {
        return "Accumulation Distribution Line";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return accumulationDistributionLine(list );
    }

    private static FloatArray accumulationDistributionLine(PriceList list) {
        FloatArray result = new FloatArray(list.size());

        result.mVal[0] = 0;
        for(int i = 1; i < list.size(); i++)
        {
            //ADL = Previous ADL + Current Period's Money Flow Volume
            result.mVal[i] = result.mVal[i-1] + list.mfv(i);
        }

        return result;
    }
}
