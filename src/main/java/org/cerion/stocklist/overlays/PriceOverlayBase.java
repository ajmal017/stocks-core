package org.cerion.stocklist.overlays;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.FunctionBase;
import org.cerion.stocklist.functions.types.IFunctionEnum;
import org.cerion.stocklist.functions.IPriceOverlay;

import java.lang.reflect.Method;

abstract class PriceOverlayBase extends FunctionBase implements IPriceOverlay {

    PriceOverlayBase(IFunctionEnum id, Number ...params) {
        super(id, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends ValueArray> getResultType() {
        try {
            Method evalMethod = getClass().getMethod("eval", PriceList.class);
            return (Class<? extends ValueArray>)evalMethod.getReturnType();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
