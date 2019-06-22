package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.Overlay;

public class ExpMovingAverage extends OverlayBase {

    public ExpMovingAverage() {
        super(Overlay.EMA, 20);
    }

    public ExpMovingAverage(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Exp. Moving Average";
    }

    @Override
    public FloatArray eval(FloatArray arr) {
        return arr.ema(getInt(0));
    }

    @Override
    public FloatArray eval(PriceList list) {
        return eval(list.mClose);
    }
}
