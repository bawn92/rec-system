package util;

public class ScoredThingDsc implements Comparable<Object>
{
	public double score;
	public Object thing;
	public boolean abs;

	public ScoredThingDsc(double s, Object t)
	{
		score = s;
		thing = t;
		abs = false;
	}

	public ScoredThingDsc(double s, Object t, boolean a)
	{
		score = s;
		thing = t;
		abs = a;
	}
	
	/* version to ensure consistency when using JRE 1.8 and earlier versions */
	public int compareTo(Object o)
	{
		ScoredThingDsc st = (ScoredThingDsc) o;
		if(abs)
			return (Math.abs(score) > Math.abs(st.score)) ? -1 : +1;
		else
		{
			if(score - st.score < -0.00001) return 1;
			else if(score - st.score >  0.00001) return -1;
			else {
				if((Integer)thing < (Integer)st.thing) return 1;
				else return -1;
			}
		}
	}
	
	
	 @Override
	    public boolean equals(Object o) {
		 ScoredThingDsc st = (ScoredThingDsc) o;			
		 	if((score == st.score)&&((Integer)thing == (Integer)st.thing))
					return true;
		 	else{
		 		return false;
		 	}
	 }

	/* original version
	public int compareTo(Object o)
	{
		ScoredThingDsc st = (ScoredThingDsc) o;
		if(abs)
			return (Math.abs(score) > Math.abs(st.score)) ? -1 : +1;
		else
			return (score > st.score) ? -1 : +1;
	}
	*/
	
	public String toString()
	{
		return "[" + score + "; " + thing + "]";
	}
}