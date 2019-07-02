package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.functions.IIndicator;
import org.cerion.stocklist.functions.ISimpleOverlay;
import org.cerion.stocklist.functions.types.Indicator;
import org.cerion.stocklist.functions.types.Overlay;
import org.cerion.stocklist.functions.types.PriceOverlay;
import org.cerion.stocklist.functions.IPriceOverlay;
import org.junit.Test;

import static org.junit.Assert.*;

public class PriceOverlayBaseTest extends FunctionTestBase {

    @Test
    public void CorrectEnumReturned() {
        for(PriceOverlay o : PriceOverlay.values()) {
            IPriceOverlay overlay = o.getInstance();
            assertEquals("enum does not match instance", o, overlay.getId());
        }
    }

    @Test
    public void ParametersVerified() {
        IPriceOverlay o = PriceOverlay.CEXIT.getInstance();

        try {
            o.setParams(22, 3.0, 5);
            fail("too many parameters");
        }
        catch(IllegalArgumentException e) {
        }

        try {
            o.setParams(22);
            fail("too few parameters");
        }
        catch(IllegalArgumentException e) {
        }

        try {
            o.setParams(22.0, 5);
            fail("wrong parameter types");
        }
        catch(IllegalArgumentException e) {
        }

        o.setParams(10, 1.0);
    }
}