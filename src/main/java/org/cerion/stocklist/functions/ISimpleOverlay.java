package org.cerion.stocklist.functions;

import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;

public interface ISimpleOverlay extends IOverlay {
    ValueArray eval(FloatArray arr);
}
