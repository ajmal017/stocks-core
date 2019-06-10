package org.cerion.stocklist.overlays;


import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.types.Overlay;

public class LinearRegressionLine extends OverlayBase {

    public LinearRegressionLine() {
        super(Overlay.LINREG);
    }

    public LinearRegressionLine(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Linear Regression Line";
    }

    @Override
    public FloatArray eval(FloatArray arr) {
        return arr.linearRegressionLine();
    }
}
