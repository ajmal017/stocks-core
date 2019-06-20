package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.PriceOverlay;
import org.cerion.stocklist.indicators.AverageTrueRange;

public class ChandelierExit extends PriceOverlayBase {

    public ChandelierExit() {
        super(PriceOverlay.CEXIT, 22, 3.0);
    }

    public ChandelierExit(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public PairArray eval(PriceList list) {
        return chandelierExit(list, getInt(0), getFloat(1));
    }

    @Override
    public String getName() {
        return "Chandelier Exit";
    }

    private PairArray chandelierExit(PriceList list, int period, float multiplier) {
        int size = list.size();

        FloatArray high = new FloatArray(size);
        FloatArray low = new FloatArray(size);
        FloatArray atr = new AverageTrueRange(period).eval(list);

        for(int i = 0; i < size; i++)
        {
            int p = ValueArray.maxPeriod(i, period);

            float h = list.mHigh.max(i-p+1, i); // highest high
            float l = list.mLow.min(i-p+1, i); // lowest low

            high.mVal[i] = h - (atr.get(i) * multiplier);
            low.mVal[i] = l + (atr.get(i) * multiplier);
        }

        return new PairArray(high, low);
    }
}
