package org.cerion.stocklist.charts;

import org.cerion.stocklist.arrays.FloatArray;

import java.awt.*;

public class DataSet implements IDataSet {

    // TODO label each data set as if Legend couldn't be used
    // TODO add color, default is rotating on any that don't have fixed amount
    private FloatArray mValues;
    private LineType mLineType = LineType.LINE;
    private String mLabel;
    private int mColor;

    @Deprecated
    protected DataSet(String label, int color) {
        mLabel = label;
        mColor = color;
    }

    protected DataSet(FloatArray arr, String label, int color) {
        this(label, color);
        mValues = arr;
    }

    public int size() {
        return mValues.getSize() - 1;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public float get(int pos) {
        return mValues.get(pos + 1);
    }

    protected void setLineType(LineType lineType) {
        mLineType = lineType;
    }

    public LineType getLineType() {
        return mLineType;
    }

    public String getLabel() {
        return mLabel;
    }
}
