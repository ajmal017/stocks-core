package org.cerion.stocklist.charts;

import org.cerion.stocklist.arrays.FloatArray;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataSetTest {

    @Test
    public void sizeOffsetByOne() {
        FloatArray arr = new FloatArray(5);
        DataSet data = new DataSet(arr, "", 0);

        assertEquals("size should be 1 less", arr.getSize()-1, data.size());
        assertEquals("invalid value at position 0", arr.get(1), data.get(0), 0.0001);
    }
}