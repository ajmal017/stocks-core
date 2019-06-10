package org.cerion.stocklist.charts;

import org.cerion.stocklist.PriceList;

public class CandleDataSet implements IDataSet {

    private PriceList mList;
    private String mLabel;
    private int mLabelColor;

    protected CandleDataSet(PriceList list, String label, int labelColor) {
        mList = list;
        mLabel = label;
        mLabelColor = labelColor;
    }

    @Override
    public int size() {
        return mList.size() - 1;
    }

    @Override
    public LineType getLineType() {
        return LineType.CANDLE;
    }

    @Override
    public String getLabel() {
        return mLabel;
    }

    public int getColor() {
        return mLabelColor;
    }

    public float getHigh(int pos) {
        return mList.mHigh.get(pos+1);
    }

    public float getLow(int pos) {
        return mList.mLow.get(pos+1);
    }

    public float getOpen(int pos) {
        return mList.mOpen.get(pos+1);
    }

    public float getClose(int pos) {
        return mList.close(pos+1);
    }


}
