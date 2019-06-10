package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class TrueStrengthIndex extends IndicatorBase {

    public TrueStrengthIndex() {
        super(Indicator.TSI, 25, 13);
    }

    public TrueStrengthIndex(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "True Strength Index";
    }

    @Override
    public FloatArray eval(PriceList list) {
        return trueStrengthIndex(list, getInt(0), getInt(1) );
    }

    private static FloatArray trueStrengthIndex(PriceList list, int p1, int p2) {
        int size = list.size();
        FloatArray result = new FloatArray(list.size());
    	/*
    	-Double Smoothed PC
    	PC = Current Price less Prior Price
    	First Smoothing = 25-period ExpMovingAverage of PC
    	Second Smoothing = 13-period ExpMovingAverage of 25-period ExpMovingAverage of PC

    	-Double Smoothed Absolute PC
    	Absolute Price Change |PC| = Absolute Value of Current Price less Prior Price
    	First Smoothing = 25-period ExpMovingAverage of |PC|
    	Second Smoothing = 13-period ExpMovingAverage of 25-period ExpMovingAverage of |PC|

    	TSI = 100 x (Double Smoothed PC / Double Smoothed Absolute PC)
    	*/

        FloatArray PC = new FloatArray(size);
        FloatArray PCabs = new FloatArray(size);
        for(int i = 1; i < size; i++) {
            PC.mVal[i] = list.close(i) - list.close(i-1);
            PCabs.mVal[i] = Math.abs(PC.get(i));
        }

        // Smoothing
        PC = PC.ema(p1).ema(p2);
        PCabs = PCabs.ema(p1).ema(p2);

        // Let first 2 values be 0
        for(int i = 2; i < size; i++) {
            result.mVal[i] = 100 * (PC.get(i) / PCabs.get(i));
        }

        return result;
    }
}
