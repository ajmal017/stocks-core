package org.cerion.stocklist.indicators;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.FunctionBase;
import org.cerion.stocklist.functions.types.IFunctionEnum;
import org.cerion.stocklist.functions.IIndicator;
import org.cerion.stocklist.functions.types.Indicator;

import java.lang.reflect.Method;

abstract class IndicatorBase extends FunctionBase implements IIndicator {

    IndicatorBase(IFunctionEnum id, Number ...params) {
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

    @Override
    public Indicator getId() {
        return (Indicator)super.getId();
    }
}
