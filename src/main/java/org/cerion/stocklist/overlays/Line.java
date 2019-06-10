package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Overlay;

public class Line extends OverlayBase {

    public Line() {
        super(Overlay.LINE, 1.0);
    }

    public Line(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Line";
    }

    @Override
    public FloatArray eval(FloatArray arr) {
        return arr.line(getFloat(0));
    }
}
