package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Overlay;

public class SimpleMovingAverage extends OverlayBase<FloatArray> {

    public SimpleMovingAverage() {
        super(Overlay.SMA, 50);
    }

    public SimpleMovingAverage(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Simple Moving Average";
    }

    @Override
    public FloatArray eval(FloatArray arr) {
        return arr.sma(getInt(0));
    }
}
