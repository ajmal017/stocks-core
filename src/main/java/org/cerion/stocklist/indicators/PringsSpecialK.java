package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class PringsSpecialK extends IndicatorBase {

    public PringsSpecialK() {
        super(Indicator.SPECIALK);
    }

    @Override
    public String getName() {
        return "Pring's Special K";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return specialK(list);
    }

    private static FloatArray specialK(PriceList list) {
        FloatArray result = new FloatArray(list.size());

		/*
		Special K = 10 Period Simple Moving Average of ROC(10) * 1
	            + 10 Period Simple Moving Average of ROC(15) * 2
	            + 10 Period Simple Moving Average of ROC(20) * 3
	            + 15 Period Simple Moving Average of ROC(30) * 4

	            + 50 Period Simple Moving Average of ROC(40) * 1
	            + 65 Period Simple Moving Average of ROC(65) * 2
	            + 75 Period Simple Moving Average of ROC(75) * 3
	            +100 Period Simple Moving Average of ROC(100)* 4

	            +130 Period Simple Moving Average of ROC(195)* 1
	            +130 Period Simple Moving Average of ROC(265)* 2
	            +130 Period Simple Moving Average of ROC(390)* 3
	            +195 Period Simple Moving Average of ROC(530)* 4
	    */

        //This is just 3 different versions of knowSureThing so it can be calculated easy
        FloatArray kst1 = list.kst(10, 15, 20, 30,     10, 10, 10, 15);
        FloatArray kst2 = list.kst(40, 65, 75, 100,    50, 65, 75, 100);
        FloatArray kst3 = list.kst(195, 265, 390, 530, 130, 130, 130, 195);

        for(int i = 0; i < list.size(); i++)
            result.mVal[i] = kst1.get(i) + kst2.get(i) + kst3.get(i);

        return result;
    }
}
