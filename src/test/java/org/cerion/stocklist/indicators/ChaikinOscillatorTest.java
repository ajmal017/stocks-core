package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChaikinOscillatorTest extends FunctionTestBase {

    @Test
    public void chaikinOscillator_test_defaults() {
        FloatArray arr = new ChaikinOscillator().eval(mPriceList);

        // TODO verify values online, just doing these pre-refactor
        assertEqual(0, arr.first(), "first");
        assertEqual(-298935.09, arr.get(1), "position 1");
        assertEqual(-265271.78, arr.get(2), "position 2");
        assertEqual(550272.0, arr.last(), "last");
    }
}