package org.cerion.stocklist.functions;

import org.cerion.stocklist.functions.types.IFunctionEnum;

import java.util.Arrays;

public abstract class FunctionBase implements IFunction {

    private Number[] mParams;
    private final IFunctionEnum mId;

    protected FunctionBase(IFunctionEnum id, Number ...params) {
        mId = id;
        mParams = removeDoubles(params);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mId.getOrdinal();
        result = prime * result + Arrays.hashCode(mParams);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        FunctionBase other = (FunctionBase) obj;
        if (mId != other.mId)
            return false;

        // Finally, equal if parameters are equal
        return  Arrays.equals(mParams, other.mParams);
    }

    @Override
    public String toString() {
        // String.join isn't working on android with a Set, possibly java v7/8 issue
        String join = getId().toString();

        int i = 0;
        for(Number n : mParams) {
            if(i > 0)
                join += ",";
            else
                join += " ";
            join += n;
            i++;
        }

        return join;
    }

    @Override
    public Number[] params() {
        return mParams;
    }

    @Override
    public IFunctionEnum getId() {
        return mId;
    }

    protected float getFloat(int pos) {
        return mParams[pos].floatValue();
    }

    protected int getInt(int pos) {
        return mParams[pos].intValue();
    }

    public void setParams(Number[] params) {
        params = removeDoubles(params);

        if(mParams.length != params.length)
            throw new IllegalArgumentException("invalid parameter count");

        for(int i = 0; i < params.length; i++) {
            if(params[0].getClass() != mParams[0].getClass())
                throw new IllegalArgumentException("invalid parameter type at position " + i);
        }
        mParams = params;
    }

    private Number[] removeDoubles(Number[] params) {
        for(int i = 0; i < params.length; i++) {
            if(params[i].getClass() == Double.class)
                params[i] = params[i].floatValue();
        }

        return params;
    }
}
