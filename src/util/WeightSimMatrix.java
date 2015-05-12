package util;

import java.util.HashMap;
import java.util.Map;

public class WeightSimMatrix 
{
	private Map<Integer,Map<Integer,Map<Integer,Double>>> matrix; // the data structure used to store case similarities
	
	/**
	 * constructor - creates a new Matrix object
	 */
	public WeightSimMatrix()
	{
		matrix = new HashMap<Integer,Map<Integer,Map<Integer,Double>>>();
	}
	
	/**
	 * adds an element  to the matrix
	 * @param rowId - the row id
	 * @param colId - the column id
	 */
	public void addElement(final Integer userId, final Integer rowId, final Integer colId, final Double entry)
	{
		Map<Integer,Map<Integer,Double>> user = (matrix.containsKey(userId)) ? matrix.get(userId) : new HashMap<Integer,Map<Integer,Double>>();
		matrix.put(userId, user);
		Map<Integer,Double> row = (matrix.get(userId).containsKey(rowId)) ? matrix.get(userId).get(rowId) : new HashMap<Integer,Double>();
		row.put(colId, entry);
		user.put(rowId, row);
		matrix.put(userId, user);
	}
	
	/**
	 * @param rowId - the row id
	 * @param colId - the column id
	 * @return the (Double) element corresponding to (rowId, colId) or null if the element is not present in the matrix
	 */
	public Double getElement(final Integer userId, final Integer rowId, final Integer colId)
	{
		return (matrix.get(userId).containsKey(rowId)) ? matrix.get(userId).get(rowId).get(colId) : null;
	}
	
	/**
	 * @return a string representation of the object
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		for(Integer rowId: matrix.keySet())
			buf.append(rowId + " " + matrix.get(rowId) + "\n");
		return buf.toString();
	}
}
