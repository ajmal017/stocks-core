package org.cerion.stocklist.indicators;


import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Indicator;

public class SharpeRatio extends IndicatorBase {

    public SharpeRatio() {
        super(Indicator.SHARPE, 10, 0.75); // default 0.75 estimate of 3-month US treasury
    }

    public SharpeRatio(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public FloatArray eval(PriceList list) {
        int years = getInt(0);
        int multiplier;
        switch (list.getInterval()) {
            case DAILY: multiplier = 252; break;
            case WEEKLY: multiplier = 52; break;
            case MONTHLY: multiplier = 12; break;
            case QUARTERLY: multiplier = 4; break;
            case YEARLY: multiplier = 1; break;
            default:
                throw new RuntimeException("unexpected interval " + list.getInterval());
        }

        FloatArray change = list.getClose().getPercentChange();
        float riskFree = (getFloat(1) / 100) / multiplier;

        for(int i = 1; i < change.size(); i++) {
            change.mVal[i] = change.mVal[i] - riskFree;
        }

        FloatArray avg = change.sma(years * multiplier);
        FloatArray std = change.std(years * multiplier);

        FloatArray result = new FloatArray(list.size());
        for(int i = 0; i < list.size(); i++) {
            if(i >= multiplier) {
                result.mVal[i] = avg.get(i) / std.get(i);
                result.mVal[i] *= Math.sqrt(multiplier);
            } else
                result.mVal[i] = Float.NaN;
        }

        return result;
    }

    @Override
    public String getName() {
        return "Sharpe Ratio";
    }
}
