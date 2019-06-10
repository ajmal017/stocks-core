package org.cerion.stocklist.arrays;

public class PairArray extends ValueArray {

	//Refer to these as positive and negative, BandArray will use upper/lower so the differences are more clear
	private FloatArray mPositive; //Higher value OR positive direction value (DI+, VI+, Aroon Up)
	private FloatArray mNegative; //Lower value OR negative direction value  (DI-, VI-, Aroon Down)

	@Override
	public int size() 
	{ 
		return mPositive.size();
	}

	/** Pair of related lines with opposing directional meaning
	 * @param positive Higher value or positive directional value
	 * @param negative Lower value or negative directional value
	 */
	public PairArray(FloatArray positive, FloatArray negative)
	{
		mPositive = positive;
		mNegative = negative;
	}
	
	
	/*
	 * Get upper or positive direction value
	 */
	public float getPos(int pos) {
		return mPositive.get(pos);
	}
	
	/*
	 * Get lower or negative direction value
	 */
	public float getNeg(int pos) {
		return mNegative.get(pos);
	}
	
	public float diff(int pos) {
		return mPositive.get(pos) - mNegative.get(pos);
	}

	public FloatArray getPositive() {
		return mPositive;
	}

	public FloatArray getNegative() {
		return mNegative;
	}
}
