package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

public class TrueStrengthIndexTest extends FunctionTestBase {

    @Test
    public void eval() {
        FloatArray arr = new TrueStrengthIndex(20,10).eval(mPriceList);
        assertEqual(0.0, arr.get(0), "tsi 0");
        assertEqual(0.0, arr.get(1), "tsi 1");
        assertEqual(-94.56, arr.get(2), "tsi 2");
        assertEqual(-26.05, arr.get(18), "tsi p-2");
        assertEqual(-24.36, arr.get(19), "tsi p-1");
        assertEqual(-20.65, arr.get(20), "tsi p");
        assertEqual(-36.77, arr.get(200), "tsi 200");
        assertEqual(-0.92, arr.last(), "tsi last");
    }
}