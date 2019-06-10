package org.cerion.stocklist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.cerion.stocklist.arrays.BandArray;

public class Price implements Comparable<Price>
{
	//Core fields
	public java.util.Date date;
	public float open;
	public float high;
	public float low;
	public float volume;
	public float close;
	
	//Fields used in list
	public PriceList parent;
	public int pos;
	public int rank; //for sector
	
	public float stochRSI() { return parent.stochRSI(14).get(pos); }
	
	public float sto() { return parent.stoch(14).get(pos); }
	public float stoFastD() { return parent.stoch(14,3).get(pos); }
	public float stoSlowD() { return parent.stoch(14,3,3).get(pos); }

	//Misc indicators
	public float pSAR() { return parent.pSAR(0.02f,0.2f).get(pos); }
	public float cci() { return parent.cci(20).get(pos); }
	public float massindex() { return parent.massIndex(25).get(pos); }
	public float ulcer() { return parent.ulcer(14).get(pos); }
	
	public float tsi() { return parent.tsi(25,13).get(pos); }
	public float adx() { return parent.adx(14).get(pos); } //Average directional index
	public float pDI() { return parent.di(14).getPos(pos); } //+DI
	public float mDI() { return parent.di(14).getNeg(pos); } //-DI
	
	public float trix() { return parent.trix(15).get(pos); }
	
	public float vortexPos() { return parent.vortex(14).getPos(pos); };
	public float vortexNeg() { return parent.vortex(14).getNeg(pos); };
	public float vx() { return (vortexPos() - vortexNeg()) * 100; }
	public float cexitLong() { return parent.cexit(22, 3.0f).getPos(pos); }
	public float cexitShort() { return parent.cexit(22, 3.0f).getNeg(pos); }
	
	public float cloudSpanA() { return parent.cloud(9, 26, 52).getPos(pos); };
	public float cloudSpanB() { return parent.cloud(9, 26, 52).getNeg(pos); };
	
	public float pc_center() { return parent.chan(20).mid(pos); } //Price Channels

	//Oscillator
	public float aroon() { return parent.aroon(25).diff(pos); };
	public float pmo() { return parent.pmo(35, 20).get(pos); }; //Price Momentum Oscillator
	public float uo() { return parent.uo(7,14,28).get(pos); }

	//Other
	public float specialK() { return parent.specialK().get(pos); }
	public float kst() { return parent.kst(10, 15, 20, 30, 10, 10, 10, 15).get(pos); }

	
	//MACD based
	public float macd() { return parent.macd(12, 26, 9).get(pos); }
	public float macdSignal() { return parent.macd(12, 26, 9).signal(pos); }
	public float macdHist() { return parent.macd(12, 26, 9).hist(pos); }
	public float ppo() { return parent.ppo(12, 26, 9).get(pos); } //Percentage version of MACD
	public float ppoSignal() { return parent.ppo(12, 26, 9).signal(pos); }
	public float ppoHist() { return parent.ppo(12, 26, 9).hist(pos); }
	public float pvo() { return parent.pvo(12, 26, 9).get(pos); } //Volume version of MACD
	public float pvoSignal() { return parent.pvo(12, 26, 9).signal(pos); }
	public float pvoHist() { return parent.pvo(12, 26, 9).hist(pos); }
	
	//Volume based
	//public long volume50; //SimpleMovingAverage of volume
	public float smaVolume(int period) { return parent.smaVolume(period).get(pos); }
	public float mfi() { return parent.mfi(14).get(pos); }
	public float cmf() { return parent.cmf(20).get(pos); } //Chaikin Money Flow
	public float fi() { return parent.forceIndex(13).get(pos); }
	public float emv() { return parent.emv(14).get(pos); } //Ease of movement
	public float adl() { return parent.adl().get(pos); }
	public float co() { return parent.co(3, 10).get(pos); }
	public float nvi () { return parent.nvi().get(pos); }
	public float obv() { return parent.onBalanceVolume().get(pos); }

	private static DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public String getDate() { return mDateFormat.format(date); } //When it needs to be formatted properly
	
	//Bands
	public float getBBU() { return getBBU(2.0f); }
	public float getBBL() { return getBBL(2.0f); }
	public float getBBU(float mult) { return parent.bb(20, mult).upper(pos); }
	public float getBBL(float mult) { return parent.bb(20, mult).lower(pos); }
	public boolean aboveBB() { return (close > getBBL()); }
	public boolean belowBB() { return (close < getBBL()); }
	public float percentB(int period, float multiplier) { return parent.bb(period, multiplier).percent(pos); }
	public float bandwidth(int period, float multiplier) { return parent.bb(period, multiplier).bandwidth(pos); }
	
	public float[] keltner(int ema, float mult, int atr) //Keltner Channels
	{
		BandArray bands = parent.kc(ema, mult, atr);
		float kc[] = { bands.upper(pos), bands.lower(pos) };
		return kc;
	}
	
	public float percentK(int ema, float mult, int atr) { return parent.kc(ema, mult, atr).percent(pos); } //Keltner channel version of percentB
	public float keltnerBW(int ema, float mult, int atr) { return parent.kc(ema, mult, atr).bandwidth(pos); } //Keltner bandwidth
	
	
	
	public static String getDecimal(float val)
	{
		return String.format("%.2f",val);
	}

	@Deprecated
	public Price(java.util.Date date, float open, float high, float low, float close, long volume)
	{
		this.date = date;
		this.open = open;
		this.high = high;
		this.low  = low;
		this.volume = volume;
		this.close = close;
		
		//Error checking
		if(open < low || close < low || open > high || close > high)
			throw new RuntimeException("Price range inconsistency " + String.format("%s,%f,%f,%f,%f", getDate(), open, high, low, close));

	}

	public Price(java.util.Date date, float open, float high, float low, float close, float volume) {
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.close = close;

		//Error checking
		if(open < low || close < low || open > high || close > high)
			throw new RuntimeException("Price range inconsistency " + String.format("%s,%f,%f,%f,%f", getDate(), open, high, low, close));
	}
	
	public int getDOW()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	@Deprecated
	public float avgGain()
	{
		return avgGain(1);
	}

	@Deprecated
	public float avgGain(int days)
	{
		float gain = 0;
		int end = pos + days;
		
		for(int i = pos+1; i <= end; i++)
		{
			Price t = parent.get(i);
			gain += t.getPercentDiff(this);
		}
		
		gain /= days;	
		
		if(gain > 1000)
			gain = 10; //Bug in PLL
		return gain;
	}
	
	public float sma(int period) { return parent.sma(period).get(pos); } //Simple moving average
	public float stddev(int period) { return parent.getClose().std(period).get(pos); } //Standard deviation
	
	public float stddevN(int period) //Normalized
	{
		return stddev(period) / sma(period);
	}
	
	public float ema(int period) { return parent.ema(period).get(pos); } //Exponential moving average
	public float rsi(int period) { return parent.rsi(period).get(pos); } //Relative Strength Index
	public float atr(int period) { return parent.atr(period).get(pos); } //average true range
	
	public float tp() { return parent.tp(pos); } //Typical price
	public float mfv() { return parent.mfv(pos); } //Money flow volume
	public float roc(int period) { return parent.roc(pos, period); }; //Rate of change

	// TODO add this as indicator class
	public float copp(int roc1, int roc2, int period) //Coppock Curve
	{
		int total = 0;
		for(int i = pos - period + 1; i <= pos; i++)
			total += roc(roc1) + roc(roc2);
	
		return total / period;
	
	}
	
	public float change(Price prev)
	{
		return getPercentDiff(prev);
	}
	public float getPercentDiff(Price old)
	{
		if(old.date.before(date) == false)
			throw new RuntimeException("current price is older than input price");
		
		float diff = close - old.close;
		float percent = (100 * (diff / old.close));
		return percent;
	}
	
    public float tr() { return parent.tr(pos); } //True Range
    public float slope(int period) { return parent.slope(period, pos); } //Slope of closing price
    
	public float getRatio(float avg) //Bias ratio
	{
		if(avg == 0)
			throw new ArithmeticException("Divide by zero");

		float diff = close - avg;
		return (100 * (diff / avg));
	}
	
	public float emaRatio(int period)
	{
		return getRatio(ema(period));
	}
	
	public float wpr(int period) { return parent.wpr(period).get(pos); } //Williams %R

	public float getBin()
	{
		Random rand = new Random();

		float bin = rand.nextFloat() / 10;//0.01f;
		
		
		if(rsi(3) > 57.19) bin += 1;
		if( getRatio( sma(200) ) > 19.01) bin +=2;
		//if(vx() > 11.16) bin += 2;
		//if(wpr(10) > -37.10) bin += 4;
		//if(slope(5) > 0.08) bin += 8;

		return bin;
	}

	@Override
	public int compareTo(Price p) {
		//Sort by date ascending, oldest first
		return this.date.compareTo(p.date);
	}
}
