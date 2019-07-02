package org.cerion.stocklist.overlays;

import org.cerion.stocklist.arrays.BandArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.FunctionTestBase;
import org.cerion.stocklist.functions.ISimpleOverlay;
import org.cerion.stocklist.functions.types.Overlay;
import org.junit.Test;

import static org.junit.Assert.*;

public class OverlayBaseTest extends FunctionTestBase {

    @Test
    public void CorrectEnumReturned() {

        for(Overlay o : Overlay.values()) {
            ISimpleOverlay overlay = o.getInstance();
            assertEquals("enum does not match instance", o, overlay.getId());
        }
    }
}