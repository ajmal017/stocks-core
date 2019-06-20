package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.indicators.ChaikinOscillator;
import org.junit.Test;

import static org.junit.Assert.*;

public class KeltnerChannelsTest extends FunctionTestBase {

    @Test
    public void keltnerChannels_defaults() {
        BandArray arr = new KeltnerChannels().eval(mPriceList);

        int last = arr.size() - 1;
        // TODO verify values online, just doing these pre-refactor
        assertEqual(4.62, arr.bandwidth(last), "last");
    }
}