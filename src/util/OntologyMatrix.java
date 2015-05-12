package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OntologyMatrix 
{
	
	//Takes two genres as strings and stores there apperances together
	private Map<String,Map<String,Double>> matrix; // the data structure used to store case similarities
	
	/**
	 * constructor - creates a new Matrix object
	 */
	//Basically the same as the orginal matrix except with update addElement function
	public OntologyMatrix()
	{
		matrix = new HashMap<String,Map<String,Double>>();
	}
	
	/**
	 * adds an element  to the matrix
	 * @param rowId - the row id
	 * @param colId - the column id
	 */
	
	// Increases the count for a certain pair if the pair exits if it dosent create pair and set to zero
	public void addElement(final String rowId, final String colId)
	{
		Map<String,Double> row = (matrix.containsKey(rowId)) ? matrix.get(rowId) : new HashMap<String,Double>();
		Map<String,Double> row1 = (matrix.containsKey(colId)) ? matrix.get(colId) : new HashMap<String,Double>();
		
		if (row.containsKey(colId)){
			row.put(colId, row.get(colId)+1.0);
		}else{ 
			row.put(colId,1.0);
		}
		matrix.put(rowId, row);
		
		if (row1.containsKey(rowId)){
			row1.put(rowId, row1.get(rowId)+1.0);
		}else{
			row1.put(rowId, 1.0);
		}
		matrix.put(colId, row1);
	}
	
	/**
	 * @param rowId - the row id
	 * @param colId - the column id
	 * @return the (Double) element corresponding to (rowId, colId) or null if the element is not present in the matrix
	 */
	public Double getElement(final String rowId, final String colId)
	{
		return (matrix.containsKey(rowId)) ? matrix.get(rowId).get(colId) : null;
	}
	
	/**
	 * @return a string representation of the object
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		for(String rowId: matrix.keySet())
			buf.append(rowId + " " + matrix.get(rowId) + "\n");
		return buf.toString();
	}

	public void addZeros(ArrayList<String> allG) {
		
		for(int i=0;i<allG.size();i++){
			
			for(int x=0;x<allG.size();x++){
				
				if(matrix.containsKey(allG.get(i))){
					if(!matrix.get(allG.get(i)).containsKey(allG.get(x))){
						matrix.get(allG.get(i)).put(allG.get(x), 0.0);
						
					}
					
				}
				
			}
		}
		
		
	}
}
