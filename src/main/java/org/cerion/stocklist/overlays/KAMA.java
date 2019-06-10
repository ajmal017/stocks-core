package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Overlay;

public class KAMA extends OverlayBase {

    public KAMA() {
        super(Overlay.KAMA, 10, 2, 30);
    }

    public KAMA(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Adaptive Moving Average";
    }

    @Override
    public FloatArray eval(FloatArray arr) {
        return arr.kama(getInt(0), getInt(1), getInt(2));
    }
}
