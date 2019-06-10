package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Overlay;

public class BollingerBands extends OverlayBase {

    public BollingerBands() {
        super(Overlay.BB, 20, 2.0);
    }

    public BollingerBands(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Bollinger Bands";
    }

    @Override
    public BandArray eval(FloatArray arr) {
        return arr.bb(getInt(0), getFloat(1));
    }
}
