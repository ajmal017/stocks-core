package org.cerion.stocklist.functions.types;

import org.cerion.stocklist.functions.ISimpleOverlay;
import org.cerion.stocklist.overlays.*;

public enum Overlay implements IFunctionEnum {

    EMA,
    SMA,
    BB,
    KAMA,
    LINE,
    LINREG;

    public ISimpleOverlay getInstance() {
        switch(this) {

            case EMA: return new ExpMovingAverage();
            case SMA: return new SimpleMovingAverage();
            case BB: return new BollingerBands();
            case KAMA: return new KAMA();
            case LINE: return new Line();
            case LINREG: return new LinearRegressionLine();

        }

        throw new RuntimeException(getClass().getSimpleName() + " missing case " + this.toString());
    }

    public ISimpleOverlay getInstance(Number ...params) {
        ISimpleOverlay overlay = getInstance();
        overlay.setParams(params);
        return overlay;
    }

    /*
    @Override
    public FunctionDef getDef() {
        switch(this)
        {
            case SimpleMovingAverage:         return new FunctionDef(this, "Simple Moving Average",   new Number[] { 50 }, FloatArray.class);
            case ExpMovingAverage:         return new FunctionDef(this, "Exp. Moving Average",     new Number[] { 20 }, FloatArray.class);
            case KAMA:        return new FunctionDef(this, "Adaptive Moving Average", new Number[] { 10, 2, 30 }, FloatArray.class);
            case BB:          return new FunctionDef(this, "Bollinger Bands", new Number[] { 20, 2.0 }, BandArray.class);
            case LINE:        return new FunctionDef(this, "Line", new Number[] { 1.0 }, FloatArray.class);
            case LINREG:      return new FunctionDef(this, "Linear Regression Line", new Number[] {}, FloatArray.class);
        }

        throw new RuntimeException(getClass().getSimpleName() + " missing case " + this.toString());
    }
    */
}
