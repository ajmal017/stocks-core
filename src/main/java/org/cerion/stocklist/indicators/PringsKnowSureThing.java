package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class PringsKnowSureThing extends IndicatorBase {

    public PringsKnowSureThing() {
        super(Indicator.KST, 10,15,20,30,10,10,10,15);
    }

    public PringsKnowSureThing(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Pring's Know Sure Thing";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return knowSureThing(list, getInt(0), getInt(1), getInt(2), getInt(3),
                                   getInt(4), getInt(5), getInt(6), getInt(7));
    }

    /*
    Short-term Daily = KST(10,15,20,30,10,10,10,15)
    Medium-term Weekly = KST(10,13,15,20,10,13,15,20)
    Long-term Monthly = KST(9,12,18,24,6,6,6,9)
    Default signal is 9 period SimpleMovingAverage (not ExpMovingAverage)
    */
    private static FloatArray knowSureThing(PriceList list, int roc1, int roc2, int roc3, int roc4, int sma1, int sma2, int sma3, int sma4) {
        int size = list.size();
        FloatArray result = new FloatArray(size);

        FloatArray r1 = new FloatArray(size);
        FloatArray r2 = new FloatArray(size);
        FloatArray r3 = new FloatArray(size);
        FloatArray r4 = new FloatArray(size);

		/*
		RCMA1 = 10-Period SimpleMovingAverage of 10-Period Rate-of-Change
		RCMA2 = 10-Period SimpleMovingAverage of 15-Period Rate-of-Change
		RCMA3 = 10-Period SimpleMovingAverage of 20-Period Rate-of-Change
		RCMA4 = 15-Period SimpleMovingAverage of 30-Period Rate-of-Change
		KST = (RCMA1 x 1) + (RCMA2 x 2) + (RCMA3 x 3) + (RCMA4 x 4)
		*/
        for(int i = 0; i < size; i++)
        {
            r1.mVal[i] = list.roc(i, roc1);
            r2.mVal[i] = list.roc(i, roc2);
            r3.mVal[i] = list.roc(i, roc3);
            r4.mVal[i] = list.roc(i, roc4);
        }

        //Apply SimpleMovingAverage to arrays
        r1 = r1.sma(sma1);
        r2 = r2.sma(sma2);
        r3 = r3.sma(sma3);
        r4 = r4.sma(sma4);

        for(int i = 0; i < size; i++)
            result.mVal[i] = r1.get(i) + (r2.get(i) * 2) + (r3.get(i) * 3) + (r4.get(i) * 4);

        return result;
    }
}
