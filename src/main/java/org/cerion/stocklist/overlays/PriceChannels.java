package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.PriceOverlay;

public class PriceChannels extends PriceOverlayBase {

    public PriceChannels() {
        super(PriceOverlay.CHAN, 20);
    }

    public PriceChannels(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public BandArray eval(PriceList list) {
        return priceChannels(list, getInt(0));
    }

    @Override
    public String getName() {
        return "Price Channels";
    }

    private static BandArray priceChannels(PriceList list, int period) {
        int size = list.size();
        FloatArray upper = new FloatArray(size);
        FloatArray lower = new FloatArray(size);

        upper.mVal[0] = list.getHigh().get(0);
        lower.mVal[0] = list.getLow().get(0);

        for(int i = 1; i < size; i++) {
            int p = ValueArray.maxPeriod(i, period);
            int start = Math.max(i - p, 0);
            upper.mVal[i] = list.getHigh().max(start, i - 1);
            lower.mVal[i] = list.getLow().min(start, i - 1);
        }

        return new BandArray(upper, lower);
    }
}
