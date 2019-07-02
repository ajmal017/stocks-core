package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

import static org.junit.Assert.*;


public class UltimateOscillatorTest extends FunctionTestBase {

    @Test
    public void eval() {
        FloatArray arr = new UltimateOscillator(7,14,28).eval(mPriceList);

        assertEqual(0, arr.first(), "uo 0");
        assertEqual(0, arr.get(1), "uo 1");
        assertEqual(50.21, arr.get(28), "uo 28");
        assertEqual(63.82, arr.get(arr.getSize()-2), "uo last-1");
        assertEqual(56.02, arr.last(), "uo last");
    }
}