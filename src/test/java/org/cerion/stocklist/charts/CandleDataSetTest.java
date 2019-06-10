package org.cerion.stocklist.charts;

import org.cerion.stocklist.Helper;
import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.junit.Test;

import static org.junit.Assert.*;


public class CandleDataSetTest {

    @Test
    public void sizeOffsetByOne() {
        PriceList list = Helper.getSP500TestData();
        CandleDataSet data = new CandleDataSet(list, "", 0);

        assertEquals("size should be 1 less", list.size()-1, data.size());
        assertEquals("invalid close value at position 0", list.close(1), data.getClose(0), 0.0001);
        assertEquals("invalid high value at position 0", list.high(1), data.getHigh(0), 0.0001);
        assertEquals("invalid low value at position 0", list.low(1), data.getLow(0), 0.0001);
        assertEquals("invalid open value at position 0", list.mOpen.get(1), data.getOpen(0), 0.0001);
    }
}