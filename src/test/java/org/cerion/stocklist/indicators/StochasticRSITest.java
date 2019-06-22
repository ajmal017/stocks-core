package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class StochasticRSITest extends FunctionTestBase {

    @Test
    public void stochasticRSI_defaults() {
        FloatArray arr = new StochasticRSI().eval(mPriceList);

        // TODO verify value online, just doing these pre-refactor
        assertEqual(0.57, arr.last(), "last");
    }
}