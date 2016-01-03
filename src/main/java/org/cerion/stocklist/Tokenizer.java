package org.cerion.stocklist;

class Tokenizer 
{
	//private ArrayList<String> tokens = new ArrayList<String>();
	private String mTokens[];
	private int mPos = 0;

	
	public static class Token
	{
		public static final int TYPE_STRING = 0;
		public static final int TYPE_NUMBER = 1;
		public static final int TYPE_ARRAY = 2;
		
		int type;
		Number nVal;
		String sVal;
		
		public Token(Object obj)
		{
			if(obj.getClass() == Number.class || obj.getClass() == Integer.class || obj.getClass() == Float.class)
			{
				type = TYPE_NUMBER;
				nVal = (Number)obj;
			}
			else
			{
				type = TYPE_STRING;
				sVal = (String)obj;
			}
		}
		
		@Override
		public String toString()
		{
			if(type == TYPE_NUMBER)
				return nVal.toString();
			else if(type == TYPE_ARRAY)
				return "[" + sVal + "]";
			else
				return sVal;
		}
	}
	

	public Tokenizer(String s)
	{
		String array = "";
		if(s.contains("[") && s.contains("]"))
		{
			int start = s.indexOf("[");
			int end = s.lastIndexOf("]");
			array = s.substring(start,end+1);
			
			s = s.replace(array, "array");
		}
		
		mTokens = s.split(" ");
		
		for(int i = 0; i < mTokens.length; i++)
		{
			if(mTokens[i].contentEquals("array"))
				mTokens[i] = array;
		}
		
	}
	
	public boolean hasInput()
	{
		if(mPos < mTokens.length)
			return true;
		
		return false;
	}

	public Token GetNext()
	{
		String s = mTokens[mPos++];
		
		//TODO, need a way to parse multiple arrays "ind [array] [array] number"
		if(s.startsWith("[") && s.endsWith("]"))
		{
			String arr = s.substring(1, s.length()-1);
			Token tok = new Token(arr);
			tok.type = Token.TYPE_ARRAY;
			return tok;
		}
		
		if( !isNumeric(s))
			return new Token(s);
		
		Number result;
		if(s.contains("."))
			result = Float.parseFloat(s);
		else
			result = Integer.parseInt(s);
		
		return new Token(result);
	}
	
	private static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?"); 
	}
	
}
