package org.cerion.stocklist.overlays;


import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.FloatArray;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.IFunctionEnum;
import org.cerion.stocklist.functions.ISimpleOverlay;

import java.lang.reflect.Method;

abstract class OverlayBase extends PriceOverlayBase implements ISimpleOverlay {

    OverlayBase(IFunctionEnum id, Number ...params) {
        super(id,params);
    }

    @Override
    public ValueArray eval(PriceList list) {
        return eval(list.getClose());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends ValueArray> getResultType() {
        try {
            Method evalMethod = getClass().getMethod("eval", FloatArray.class);
            return (Class<? extends ValueArray>)evalMethod.getReturnType();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
