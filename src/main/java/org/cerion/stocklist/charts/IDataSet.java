package org.cerion.stocklist.charts;

public interface IDataSet {
    int size();
    LineType getLineType();
    String getLabel();
    int getColor();
}
