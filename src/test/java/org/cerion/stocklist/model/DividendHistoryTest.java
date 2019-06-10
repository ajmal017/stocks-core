package org.cerion.stocklist.model;

import org.cerion.stocklist.TestBase;
import org.cerion.stocklist.Utils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DividendHistoryTest extends TestBase {

    @Test
    public void dates() {
        List<Dividend> dividends = Utils.getDividends(9.1f,2.2f,3.56f);
        DividendHistory history = new DividendHistory(dividends, Utils.getDate(30));

        assertEquals(9.1, history.getLastDividend(), 0.005);
        assertDateEquals(new Date(), history.getLastDividendDate());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        assertDateEquals(calendar.getTime(), history.getNextDividendEstimate());
    }
}