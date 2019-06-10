package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.functions.types.PriceOverlay;

public class IchimokuClouds extends PriceOverlayBase {

    public IchimokuClouds() {
        super(PriceOverlay.CLOUD, 9, 26, 52);
    }

    public IchimokuClouds(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public PairArray eval(PriceList list) {
        return ichimokuCloud(list, getInt(0), getInt(1), getInt(2));
    }

    @Override
    public String getName() {
        return "Ichimoku Clouds";
    }

    private static PairArray ichimokuCloud(PriceList list, int conversion, int base, int span)
    {
        int size = list.size();
        FloatArray spanA = new FloatArray(size);
        FloatArray spanB = new FloatArray(size);

        FloatArray highs = list.getHigh();
        FloatArray lows = list.getLow();

        for(int i = span; i < size; i++)
        {
            //Conversion Line
            float high = highs.max(i-conversion+1, i);
            float low  = lows.min(i-conversion+1, i);
            float conversionLine = (high + low) / 2;

            //Base line
            high = highs.max(i-base+1, i);
            low  = lows.min(i-base+1, i);
            float baseLine = (high + low) / 2;

            //Leading Span A
            spanA.mVal[i] = (conversionLine + baseLine) / 2;

            //Leading Span B
            high = highs.max(i-span+1, i);
            low  = lows.min(i-span+1, i);
            spanB.mVal[i] = (high + low) / 2;
        }

        return new PairArray(spanA,spanB);
    }
}
