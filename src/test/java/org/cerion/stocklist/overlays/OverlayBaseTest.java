package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.functions.ISimpleOverlay;
import org.cerion.stocklist.functions.types.Overlay;
import org.junit.Test;

import static org.junit.Assert.*;

public class OverlayBaseTest extends FunctionTestBase {

    @Test
    public void verifyReturnTypes() {

        for(Overlay o : Overlay.values()) {
            ISimpleOverlay overlay = o.getInstance();
            ValueArray arr = overlay.eval(mPriceList.getClose());
            assertEquals("'" + o.toString() + "' resultType() does not match eval() result", arr.getClass(), overlay.getResultType());

            // Verify when called on both evals
            arr = overlay.eval(mPriceList);
            assertEquals("'" + o.toString() + "' resultType() does not match eval() result (2)", arr.getClass(), overlay.getResultType());
        }
    }

    @Test
    public void CorrectEnumReturned() {

        for(Overlay o : Overlay.values()) {
            ISimpleOverlay overlay = o.getInstance();
            assertEquals("enum does not match instance", o, overlay.getId());
        }
    }
}