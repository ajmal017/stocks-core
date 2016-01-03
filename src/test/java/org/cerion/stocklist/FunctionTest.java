package org.cerion.stocklist;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cerion.stocklist.data.FloatArray;
import org.junit.Before;
import org.junit.Test;

public class FunctionTest {

	List<Function.Parameter> params;
	
	
	@Before
	public void setup(){
		params = new ArrayList<>();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidArgumentFloat(){
		Function.Parameter param = new Function.Parameter(new Float(2.0));
		params.add(param);
		
		Function f = new Function(null,"RSI", params);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidArgumentInteger(){
		Function.Parameter param = new Function.Parameter(new Integer(10));
		params.add(param);
		
		Function f = new Function(null,"PSAR", params);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidArgumentArray(){
		Function.Parameter param = new Function.Parameter(new FloatArray(10));
		params.add(param);
		
		Function f = new Function(null,"RSI", params);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void missingArgumentException(){
		Function f = new Function(null,"RSI", params);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidFunctionName(){
		Function f = new Function(null,"ASDF", params);
	}
	
	@Test
	public void allowVariableParameterCount() {
		params.add( new Function.Parameter(new Integer(10)) );
		Function f = new Function(null,"STOCH", params);
		
		params.add( new Function.Parameter(new Integer(10)) );
		f = new Function(null,"STOCH", params);
		
		params.add( new Function.Parameter(new Integer(10)) );
		f = new Function(null,"STOCH", params);
	}
	
}
