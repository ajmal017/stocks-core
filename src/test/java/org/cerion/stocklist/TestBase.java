package org.cerion.stocklist;

import org.junit.Assert;

import java.util.Date;

public class TestBase {

    public static void assertDateEquals(Date expected, Date actual) {
        Assert.assertEquals(expected.getTime() / 1000, actual.getTime() / 1000);
    }
}
