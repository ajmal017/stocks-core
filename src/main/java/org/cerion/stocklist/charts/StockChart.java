package org.cerion.stocklist.charts;

import org.cerion.stocklist.PriceList;
import org.cerion.stocklist.arrays.*;
import org.cerion.stocklist.functions.*;
import org.cerion.stocklist.functions.types.IFunctionEnum;
import org.cerion.stocklist.functions.types.Overlay;
import org.cerion.stocklist.model.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class StockChart implements Cloneable {

    //protected List<FunctionCall> mOverlays = new ArrayList<>();
    protected List<IOverlay> mOverlays = new ArrayList<>();

    private PriceList mList;
    public Interval interval = Interval.DAILY;
    protected int[] mPrimaryColors = new int[] { 0, 0, 0, 0 }; // TODO add colors that don't use any java libraries, android overrides them all anyway
    protected int[] mSecondaryColors = new int[] { 0 };

    @Override
    public Object clone() throws CloneNotSupportedException {
        StockChart clone = (StockChart)super.clone();

        // Copy overlays
        clone.mOverlays = new ArrayList<>();

        for(IOverlay overlay : mOverlays) {
            IFunction copy = overlay.getId().getInstance();
            copy.setParams( overlay.getParams().toArray(new Number[0]).clone());
            clone.mOverlays.add( (IOverlay)copy );
        }

        return clone;
    }

    public IFunctionEnum[] getOverlays() {
        return Overlay.values();
        /*
        List<IOverlay> result = new ArrayList<>();
        for(Overlay o : Overlay.values()) {
            result.add(o.getInstance());
        }

        return result;
        */
    }

    public StockChart addOverlay(ISimpleOverlay overlay) {
        mOverlays.add(overlay);
        return this;
    }

    public void clearOverlays() {
        mOverlays.clear();
    }

    public IOverlay getOverlay(int position) {
        return mOverlays.get(position);
    }

    public int getOverlayCount() {
        return mOverlays.size();
    }

    public void setPriceList(PriceList list) {
        mList = list;
    }

    protected int colorBlack() {
        return mPrimaryColors[0];
    }

    protected int colorRed() {
        return mPrimaryColors[1];
    }

    protected int colorBlue() {
        return mPrimaryColors[2];
    }

    protected int colorGreen() {
        return mPrimaryColors[3];
    }

    public abstract List<IDataSet> getDataSets();

    public Date[] getDates() {
        return mList.getDates();
    }

    public void setPrimaryColors(int[] colors) {
        if(colors.length == mPrimaryColors.length)
            mPrimaryColors = colors;
    }

    public void setSecondaryColors(int[] colors) {
        mSecondaryColors = colors;
    }

    protected PriceList getPriceList() {
        return getPriceList(false);
    }

    protected PriceList getPriceList(boolean logScale) {
        if(logScale)
            return mList.toLogScale();

        return mList;
    }

    private int nextColor = 0;
    protected int getNextColor() {
        return mSecondaryColors[nextColor++ % mSecondaryColors.length];
    }

    protected void resetNextColor() {
        nextColor = 0;
    }

    protected List<DataSet> getDefaultOverlayDataSets(ValueArray arr, IOverlay overlay) {
        if(arr.getClass() == BandArray.class)
            return getBandDataSet((BandArray)arr,overlay.toString(), overlay.toString(),getNextColor());
        else if(arr.getClass() == PairArray.class)
            return getPairDataSet((PairArray)arr, overlay.toString(), overlay.toString(), colorGreen(), colorRed());

        return getSingleDataSet((FloatArray)arr, overlay.toString(), getNextColor());
    }

    protected List<DataSet> getSingleDataSet(FloatArray values, String label, int color) {
        return new ArrayList<>(Arrays.asList(new DataSet(values, label, color)));
    }

    protected List<DataSet> getBandDataSet(BandArray values, String labelUpper, String labelLower, int color) {
        FloatArray upper = new FloatArray(values.getSize());
        FloatArray lower = new FloatArray(values.getSize());

        for (int i = 0; i < values.getSize(); i++) {
            upper.set(i, values.upper(i));
            lower.set(i, values.lower(i));
        }

        List<DataSet> result = new ArrayList<>();
        result.add(new DataSet(upper, labelUpper, color));
        result.add(new DataSet(lower, labelLower, color));

        return result;
    }

    protected List<DataSet> getPairDataSet(PairArray values, String labelPos, String labelNeg, int colorPos, int colorNeg) {
        List<DataSet> result = new ArrayList<>();
        result.add(new DataSet(values.getPositive(), labelPos, colorPos));
        result.add(new DataSet(values.getNegative(), labelNeg, colorNeg));
        return result;
    }

    protected List<DataSet> getMACDDataSet(MACDArray values,
                                           String labelMACD, String labelSignal, String labelHist,
                                           int colorMACD, int colorSignal, int colorHist) {

        FloatArray signal = new FloatArray(values.getSize());
        FloatArray hist = new FloatArray(values.getSize());

        // TODO make function to get signal/hist arrays directly
        for (int i = 0; i < values.getSize(); i++) {
            signal.set(i, values.signal(i));
            hist.set(i, values.hist(i));
        }

        List<DataSet> result = new ArrayList<>();
        result.add(new DataSet(values, labelMACD, colorMACD));
        result.add(new DataSet(signal, labelSignal, colorSignal));
        result.add(new DataSet(hist, labelHist, colorHist));

        return result;
    }
    /*

    static List<ILineDataSet> getPairDataSet(PairArray arr, int positiveColor, int negativeColor) {
        EntrySet pos = new EntrySet(positiveColor);
        EntrySet neg = new EntrySet(negativeColor);

        for (int i = 0; i < arr.size(); i++) {
            pos.entries.add(new Entry(i, arr.getPos(i)));
            neg.entries.add(new Entry(i, arr.getNeg(i)));
        }

        return getLineDataSets(pos, neg);
    }


    */


}
