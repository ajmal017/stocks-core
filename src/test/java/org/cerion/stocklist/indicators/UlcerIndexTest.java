package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

public class UlcerIndexTest extends FunctionTestBase {

    @Test
    public void eval() {
        FloatArray arr = new UlcerIndex(14).eval(mPriceList);

        // TODO stock charts has a different value for this but their calculation might be wrong
        // This one matches if you do sqrt twice which seems wrong
        assertEqual(0, arr.first(), "ulcer 0");
        assertEqual(1.02, arr.get(1), "ulcer 1");
        assertEqual(1.93, arr.get(13), "ulcer 13");
        assertEqual(2.25, arr.get(14), "ulcer 14");
        assertEqual(2.63, arr.last(), "ulcer last");
    }
}