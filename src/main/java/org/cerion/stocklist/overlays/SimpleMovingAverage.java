package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Overlay;

public class SimpleMovingAverage extends OverlayBase {

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

    @Override
    public FloatArray eval(PriceList list) {
        return eval(list.mClose);
    }
}
