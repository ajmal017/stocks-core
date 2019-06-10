package org.cerion.stocklist.functions.types;

import org.cerion.stocklist.functions.IFunction;

public interface IFunctionEnum {
    int ordinal();
    IFunction getInstance();
    IFunction getInstance(Number ...params);
}
