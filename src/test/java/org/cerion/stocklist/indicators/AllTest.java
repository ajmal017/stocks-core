package org.cerion.stocklist.indicators;

import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.*;
import org.cerion.stocklist.functions.types.Indicator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AllTest extends FunctionTestBase {

    @Test
    public void verifyReturnTypes() {

        for(Indicator i : Indicator.values()) {
            IIndicator instance = i.getInstance();
            ValueArray arr = instance.eval(mPriceList);

            assertEquals("'" + i.toString() + "' resultType() does not match eval() result", arr.getClass(), instance.getResultType());
        }
    }

    @Test
    public void CorrectEnumReturned() {
        for(Indicator i : Indicator.values()) {
            IIndicator instance = i.getInstance();
            assertEquals("enum does not match instance", i, instance.getId());
        }
    }
}
