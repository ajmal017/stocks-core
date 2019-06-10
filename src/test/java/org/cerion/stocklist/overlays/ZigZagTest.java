package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.overlays.ZigZag;
import org.junit.Test;


public class ZigZagTest extends FunctionTestBase {

    @Test
    public void eval() throws Exception {

        int size = mPriceList.size();
        FloatArray arr = new ZigZag(20.0).eval(mPriceList);

        assertEqual(mPriceList.high(0), arr.get(0), "zigzag start");
        assertEqual(mPriceList.low(307), arr.get(307), "zigzag first low");
        assertEqual(mPriceList.high(349), arr.get(349), "zigzag first high");
        assertEqual(mPriceList.low(2957), arr.get(2957), "zigzag last low");
        assertEqual(mPriceList.high(3868), arr.get(3868), "zigzag last high");
        assertEqual(mPriceList.low(size-1), arr.get(size-1), "zigzag end");


    }

}