package org.cerion.stocklist.functions.types;

import org.cerion.stocklist.functions.IIndicator;
import org.cerion.stocklist.indicators.*;

public enum Indicator implements IFunctionEnum {

	MACD,
	RSI,
	ADX,
	AROON,
	ATR,
	CCI,
	DI,
	KST,
	MASS_INDEX,
	PMO,
	PPO,
	SPECIALK, // prings specialK
	STOCH,
	STOCHRSI,
	TRIX,
	TSI,
	ULCER_INDEX,
	UO,
	VORTEX,
	WPR,
	SHARPE,

	// Volume Indicators
	ADL,
	CMF,
	CO,
	EMV,
	FORCE_INDEX,
	MFI,
	NVI,
	OBV,
	PVO;

	public IIndicator getInstance() {
		switch(this) {
			case MACD: return new MACD();
			case RSI: return new RSI();
			case ADX: return new AverageDirectionalIndex();
			case FORCE_INDEX: return new ForceIndex();
			case ATR: return new AverageTrueRange();
			case STOCHRSI: return new StochasticRSI();
			case CCI: return new CommodityChannelIndex();
			case MFI: return new MoneyFlowIndex();
			case CMF: return new ChaikinMoneyFlow();
			case MASS_INDEX: return new MassIndex();
			case TRIX: return new TRIX();
			case ULCER_INDEX: return new UlcerIndex();
			case EMV: return new EaseOfMovement();
			case OBV: return new OnBalanceVolume();
			case ADL: return new AccumulationDistributionLine();
			case NVI: return new NegativeVolumeIndex();
			case WPR: return new WilliamsPercentR();
			case VORTEX: return new Vortex();
			case UO: return new UltimateOscillator();
			case TSI: return new TrueStrengthIndex();
			case KST: return new PringsKnowSureThing();
			case CO: return new ChaikinOscillator();
			case SPECIALK: return new PringsSpecialK();
			case PMO: return new PriceMomentumOscillator();
			case AROON: return new AroonUpDown();
			case DI: return new DirectionalIndex();
			case PVO: return new PercentageVolumeOscillator();
			case PPO: return new PercentagePriceOscillator();
			case STOCH: return new Stochastic();
			case SHARPE: return new SharpeRatio();

		}

		throw new RuntimeException(getClass().getSimpleName() + " missing case " + this.toString());
	}

	public IIndicator getInstance(Number ...params) {
		IIndicator instance = getInstance();
		instance.setParams(params);
		return instance;
	}

	/*
	public static List<Indicator> getAll() {
		List<Indicator> result = new ArrayList<>();

		for(Indicator i : Indicator.values())
			result.add(i);

		return result;
	}
	*/

	/*
	public  getDef() {
		Indicator id = this;
		switch(id)
		{

			// TODO define default overlay, signal line in most cases
			//case RSI:         return new FunctionDef(id, "RSI",         new Number[] { 14 }, FloatArray.class);
			//case FORCE_INDEX: return new FunctionDef(id, "Force Index", new Number[] { 13 }, FloatArray.class);
			//case ADX:         return new FunctionDef(id, "Average Directional Index", new Number[] { 14 }, FloatArray.class);
			//case ATR: 	      return new FunctionDef(id, "Average True Range",        new Number[] { 14 }, FloatArray.class);
			//case CCI:         return new FunctionDef(id, "Commodity Channel Index",   new Number[] { 20 }, FloatArray.class);
			//case STOCHRSI:    return new FunctionDef(id, "Stochastic RSI",     new Number[] { 14 }, FloatArray.class);
			//case MFI: 	      return new FunctionDef(id, "Money flow index",   new Number[] { 14 }, FloatArray.class);
			//case CMF:         return new FunctionDef(id, "Chaikin Money Flow", new Number[] { 20 }, FloatArray.class);
			//case MASS_INDEX:  return new FunctionDef(id, "Mass Index",      new Number[] { 25 }, FloatArray.class);
			//case TRIX:        return new FunctionDef(id, "TRIX",            new Number[] { 15 }, FloatArray.class);
			//case ULCER_INDEX: return new FunctionDef(id, "Ulcer Index",     new Number[] { 14 }, FloatArray.class);
			//case EMV: 	      return new FunctionDef(id, "Ease of Movement",new Number[] { 14 }, FloatArray.class);
			//case WPR:         return new FunctionDef(id, "Williams %R",     new Number[] { 14 }, FloatArray.class);
			//case OBV:         return new FunctionDef(id, "On Balance Volume",     new Number[] {}, FloatArray.class);
			//case NVI:         return new FunctionDef(id, "Negative Volume Index", new Number[] {}, FloatArray.class);
			//case ADL: 	      return new FunctionDef(id, "Accumulation Distribution Line",new Number[] {}, FloatArray.class);
			//case SPECIALK:    return new FunctionDef(id, "Pring's Special K",     new Number[] {}, FloatArray.class);
			//case VORTEX:      return new FunctionDef(id, "Vortex",            new Number[] { 14 }, PairArray.class);
			//case AROON:       return new FunctionDef(id, "Aroon Up/Down",     new Number[] { 25 }, PairArray.class);
			//case DI: 	      return new FunctionDef(id, "Directional Index", new Number[] { 14}, PairArray.class);
			//case CO:          return new FunctionDef(id, "Chaikin Oscillator",        new Number[] { 3, 10 }, FloatArray.class);
			//case TSI: 	      return new FunctionDef(id, "True Strength Index",       new Number[] { 25, 13 }, FloatArray.class);
			//case PMO:         return new FunctionDef(id, "Price Momentum Oscillator", new Number[] { 35, 20 }, FloatArray.class);
			//case MACD:        return new FunctionDef(id, "MACD",                        new Number[] { 12, 26, 9 }, MACDArray.class);
			//case PPO:         return new FunctionDef(id, "Percentage Price Oscillator", new Number[] { 12, 26, 9 }, MACDArray.class);
			//case PVO: 	      return new FunctionDef(id, "Percentage Volume Oscillator",new Number[] { 12, 26, 9 }, MACDArray.class);

			//case UO:          return new FunctionDef(id, "Ultimate Oscillator",    new Number[] { 7, 14, 28 }, FloatArray.class);
			//case KST: 	      return new FunctionDef(id, "Pring's Know Sure Thing",new Number[] { 10,15,20,30,10,10,10,15 }, FloatArray.class);
			//case STOCH:       return new FunctionDef(id, "Stochastic Oscillator",  new Number[] { 14, 3, 3 }, FloatArray.class);
		}

		// New method
		IIndicator instance = getInstance();
		if(instance != null)
			return new FunctionDef(id, instance.getName(), instance.params(), instance.getResultType());

		return null;
	}
	*/
}
