package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.PairArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.junit.Test;

public class DirectionalIndexTest extends FunctionTestBase {

    @Test
    public void directionalIndex_defaults() {
        PairArray arr = new DirectionalIndex().eval(mPriceList);

        int last = arr.size();

        // TODO verify values online, just doing these pre-refactor
        assertEqual(30.29, arr.getNeg(last-1), "last");
        assertEqual(27.01, arr.getPos(last-1), "last");
    }
}