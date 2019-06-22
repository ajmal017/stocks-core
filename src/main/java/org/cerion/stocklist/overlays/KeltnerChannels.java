package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.PriceOverlay;
import org.cerion.stocklist.indicators.AverageTrueRange;

public class KeltnerChannels extends PriceOverlayBase {

    public KeltnerChannels() {
        super(PriceOverlay.KC, 20, 2.0, 10);
    }

    public KeltnerChannels(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public BandArray eval(PriceList list) {
        int emaPeriod = getInt(0);
        float multiplier = getFloat(1);
        int atrPeriod = getInt(2);

        FloatArray ema = new ExpMovingAverage(emaPeriod).eval(list);
        //Middle Line: 20-day exponential moving average
        //Upper Channel Line: 20-day ExpMovingAverage + (2 x ATR(10))
        //Lower Channel Line: 20-day ExpMovingAverage - (2 x ATR(10))
        return new BandArray(list.mClose, multiplier, ema, new AverageTrueRange(atrPeriod).eval(list));

    }

    @Override
    public String getName() {
        return "Keltner Channels";
    }
}
