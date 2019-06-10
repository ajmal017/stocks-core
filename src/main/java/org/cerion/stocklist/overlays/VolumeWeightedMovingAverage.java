package org.cerion.stocklist.overlays;


import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.PriceOverlay;

public class VolumeWeightedMovingAverage extends PriceOverlayBase {

    public VolumeWeightedMovingAverage() {
        super(PriceOverlay.VWMA, 20);
    }

    public VolumeWeightedMovingAverage(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Volume Weighted Moving Average";
    }

    @Override
    public ValueArray eval(PriceList list) {
        int size = list.size();
        int period = getInt(0);
        FloatArray result = new FloatArray(size);

        for(int i = 0; i < size; i++) {
            int count = ValueArray.maxPeriod(i, period);

            float total = 0;
            float volume = 0;
            for(int j = i-count+1; j <= i; j++) {
                volume += list.volume(j);
                total += list.close(j) * list.volume(j);
            }

            result.mVal[i] = total / volume;

        }

        return result;
    }
}
