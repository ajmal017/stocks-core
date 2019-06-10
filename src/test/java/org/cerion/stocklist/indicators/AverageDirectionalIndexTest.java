package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

public class AverageDirectionalIndexTest extends FunctionTestBase {

    @Test
    public void averageDirectionalIndex_test_14() {
        FloatArray adx = new AverageDirectionalIndex(14).eval(mPriceList);

        assertEqual(0, adx.first(), "first");
        assertEqual(50, adx.get(1), "position 1");
        assertEqual(66.67, adx.get(2), "position 2");
        assertEqual(15.04, adx.last(), "last");
    }

    @Test
    public void averageDirectionalIndex_test_7() {
        FloatArray adx = new AverageDirectionalIndex(7).eval(mPriceList);

        assertEqual(0, adx.first(), "first");
        assertEqual(50, adx.get(1), "position 1");
        assertEqual(66.67, adx.get(2), "position 2");
        assertEqual(18.20, adx.last(), "last");
    }

}