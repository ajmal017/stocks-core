package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.MACDArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

public class PercentagePriceOscillatorTest extends FunctionTestBase {

    @Test
    public void macd_temp() {
        // TODO temp to quick check PPO and PVO

        MACDArray arr = new PercentagePriceOscillator(12, 26, 9).eval(mPriceList);
        assertEqual(-0.082, arr.last(), "ppo last");
    }
}