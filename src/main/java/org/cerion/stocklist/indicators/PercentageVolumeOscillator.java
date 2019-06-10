package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.MACDArray;
import org.cerion.stocklist.functions.types.Indicator;

public class PercentageVolumeOscillator extends IndicatorBase {

    public PercentageVolumeOscillator() {
        super(Indicator.PVO, 12, 26, 9);
    }

    public PercentageVolumeOscillator(Number ...params) {
        this();
        setParams(params);
    }

    @Override
    public String getName() {
        return "Percentage Volume Oscillator";
    }

    @Override
    public MACDArray eval(PriceList list) {
        return PercentagePriceOscillator.getPercentMACD(list.mVolume, getInt(0), getInt(1), getInt(2) );
    }
}
