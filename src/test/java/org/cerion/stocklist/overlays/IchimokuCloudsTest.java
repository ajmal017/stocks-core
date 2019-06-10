package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.overlays.IchimokuClouds;
import org.junit.Test;

public class IchimokuCloudsTest extends FunctionTestBase {
    @Test
    public void eval() throws Exception {
        PairArray arr = new IchimokuClouds(9,26,52).eval(mPriceList);

        assertEqual(2046.11, arr.getPos(mSize-1), "ichimokuCloud SpanA last");
        assertEqual(2054.87, arr.getNeg(mSize-1), "ichimokuCloud SpanB last");

        arr =  new IchimokuClouds(5, 15, 30).eval(mPriceList);
        assertEqual(2050.0, arr.getPos(mSize-1), "ichimokuCloud SpanA last with different parameters");
        assertEqual(2048.77, arr.getNeg(mSize-1), "ichimokuCloud SpanB last with different parameters");
    }

}