package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class PringsSpecialKTest extends FunctionTestBase {

    @Test
    public void pringsSpecialK_test() {
        FloatArray arr = new PringsSpecialK().eval(mPriceList);

        // TODO verify values online, just doing these pre-refactor
        assertEqual(0, arr.first(), "first");
        assertEqual(-57.52, arr.get(1), "position 1");
        assertEqual(-74.84, arr.get(2), "position 2");
        assertEqual(112.63, arr.last(), "last");
    }
}