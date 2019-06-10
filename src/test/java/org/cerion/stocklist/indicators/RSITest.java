package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class RSITest extends FunctionTestBase {

    @Test
    public void eval() {
        FloatArray rsi = new RSI(14).eval(mPriceList);

        assertEqual(50, rsi.get(0), "p0");
        assertEqual(33.33, rsi.get(1), "p1");
        assertEqual(34.39, rsi.get(2), "p2");
        assertEqual(47.57, rsi.last(), "last");
    }

}