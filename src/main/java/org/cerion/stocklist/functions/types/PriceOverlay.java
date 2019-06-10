package org.cerion.stocklist.functions.types;

import org.cerion.stocklist.functions.IPriceOverlay;
import org.cerion.stocklist.overlays.*;

public enum PriceOverlay implements IFunctionEnum {

    CHAN, // price channels
    CEXIT,
    KC,
    PSAR,
    CLOUD, // Ichimoku Clouds
    ZIGZAG,
    VWMA;

    public IPriceOverlay getInstance(Number ...params) {
        IPriceOverlay overlay = getInstance();
        overlay.setParams(params);
        return overlay;
    }

    @Override
    public IPriceOverlay getInstance() {
        switch(this) {
            case CHAN: return new PriceChannels();
            case CLOUD: return  new IchimokuClouds();
            case CEXIT: return new ChandelierExit();
            case KC: return new KeltnerChannels();
            case PSAR: return new ParabolicSAR();
            case ZIGZAG: return new ZigZag();
            case VWMA: return new VolumeWeightedMovingAverage();
        }

        throw new RuntimeException(getClass().getSimpleName() + " missing case " + this.toString());
    }
}
