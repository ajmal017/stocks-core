package org.cerion.stocklist.functions;

import org.cerion.stocklist.Helper;
import org.cerion.stocklist.PriceList;
import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;

public class FunctionTestBase {

    public static PriceList mPriceList;
    public static int mSize;

    @BeforeClass
    public static void init() {
        mPriceList = Helper.getSP500TestData();
        mSize = mPriceList.size();
    }

    public static void assertEqual(double expected, float actual, String message)
    {
        assertEquals(message, expected, actual, 0.005);
    }
}
