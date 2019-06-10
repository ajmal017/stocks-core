package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.functions.types.Indicator;

public class DirectionalIndex extends IndicatorBase {

    public DirectionalIndex() {
        super(Indicator.DI, 14);
    }

    public DirectionalIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Directional Index";
    }

    @Override
    public PairArray eval(PriceList list) {
        return directionalIndex(list, getInt(0) );
    }

    private static PairArray directionalIndex(PriceList list, int period)
    {
        int size = list.size();
        FloatArray mDI = new FloatArray(size); //-DI
        FloatArray pDI = new FloatArray(size); //+DI

        float[][] trdm = new float[size][2]; //+DM / -DM

        for(int i = 1; i < size; i++)
        {
            int curr = i;
            int prev = i-1;

            //TODO, add DM function to PriceList so this can be calculated directly
            if(list.high(curr) - list.high(prev) > list.low(prev) - list.low(curr))
                trdm[i][0] = Math.max( list.high(curr) - list.high(prev), 0);

            if(list.low(prev) - list.low(curr) > list.high(curr) - list.high(prev))
                trdm[i][1] = Math.max(list.low(prev) - list.low(curr),0);
        }

        float[][] trdm14 = new float[size][3]; //TR14 / +DM14 / -DM14

        for(int i = 1; i < size; i++)
        {
            trdm14[i][0] = trdm14[i-1][0] - (trdm14[i-1][0]/period) + list.tr(i);
            trdm14[i][1] = trdm14[i-1][1] - (trdm14[i-1][1]/period) + trdm[i][0];
            trdm14[i][2] = trdm14[i-1][2] - (trdm14[i-1][2]/period) + trdm[i][1];

            pDI.mVal[i] = 100 * (trdm14[i][1] / trdm14[i][0]);
            mDI.mVal[i] = 100 * (trdm14[i][2] / trdm14[i][0]);
        }


        return new PairArray(pDI, mDI);
    }
}
