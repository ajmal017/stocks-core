package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Indicator;

public class Vortex extends IndicatorBase {

    public Vortex() {
        super(Indicator.VORTEX, 14);
    }

    public Vortex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Vortex";
    }

    @Override
    public PairArray eval(PriceList list) {
        return vortex(list, getInt(0) );
    }

    private static PairArray vortex(PriceList list, int period) {
        int size = list.size();
        FloatArray posVI = new FloatArray(size);
        FloatArray negVI = new FloatArray(size);

        float[][] vm = new float[size][2]; // +VM/-VM

        for(int i = 1; i < size; i++) {
            vm[i][0] = Math.abs(list.high(i) - list.low(i-1));
            vm[i][1] = Math.abs(list.low(i) - list.high(i-1));
        }

        // Start at 1 since that is the average value
        posVI.mVal[0] = 1;
        negVI.mVal[0] = 1;

        for(int i = 1; i < size; i++) {
            int count = ValueArray.maxPeriod(i, period);
            float vip = 0;
            float vin = 0;
            float tr = 0;
            for(int j = i - count + 1; j <= i; j++)
            {
                vip += vm[j][0];
                vin += vm[j][1];
                tr += list.tr(j);
            }

            posVI.mVal[i] = vip / tr;
            negVI.mVal[i] = vin / tr;
        }

        return new PairArray(posVI, negVI);
    }

}
