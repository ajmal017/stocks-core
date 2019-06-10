package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.overlays.PriceChannels;
import org.junit.Test;

public class PriceChannelsTest extends FunctionTestBase {

    @Test
    public void eval() throws Exception {
        BandArray arr = new PriceChannels(20).eval(mPriceList);

        assertEqual(mPriceList.high(0), arr.upper(0), "priceChannels 0");
        assertEqual(mPriceList.low(0), arr.lower(0), "priceChannels 0");
        assertEqual(1478.0, arr.upper(1), "priceChannels 1 upper");
        assertEqual(1438.36, arr.lower(1), "priceChannels 1 lower");
        assertEqual(1478.0, arr.upper(18), "priceChannels 18");
        assertEqual(1478.0, arr.upper(19), "priceChannels 19");
        assertEqual(1350.14, arr.lower(20), "priceChannels 20");
        assertEqual(2104.27, arr.upper(mSize-1), "priceChannels upper last");
        assertEqual(1993.26, arr.lower(mSize-1), "priceChannels lower last");

        arr = new PriceChannels(100).eval(mPriceList);
        assertEqual(2116.48, arr.upper(mSize-1), "priceChannels upper last with different parameters");
        assertEqual(1867.01, arr.lower(mSize-1), "priceChannels lower last with different parameters");
    }

}