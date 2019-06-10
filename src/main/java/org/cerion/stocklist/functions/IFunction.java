package org.cerion.stocklist.functions;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.ValueArray;
import org.cerion.stocklist.functions.types.IFunctionEnum;

public interface IFunction {
    ValueArray eval(PriceList list);
    String getName();
    Class<? extends ValueArray> getResultType();
    Number[] params();
    void setParams(Number ...params);
    IFunctionEnum getId();
}
