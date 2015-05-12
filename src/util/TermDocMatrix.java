package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TermDocMatrix 
{
	private Map<String,Map<Integer,Double>> matrix; // the data structure used to store case similarities
	//Store max occuring word in doc
	private Map<Integer,Double> maxOccur;
	//private Map<String,Double> termOccur;
	
	/**
	 * constructor - creates a new Matrix object
	 */
	public TermDocMatrix()
	{
		matrix = new HashMap<String,Map<Integer,Double>>();
		maxOccur = new HashMap<Integer,Double>();
	}
	
	/**
	 * adds an element  to the matrix
	 * @param term - the row id
	 * @param movieId - the column id
	 */
	public void addElement(final String term, final Integer movieId)
	{

		Map<Integer,Double> temp =new HashMap<Integer,Double>();
		temp.put(movieId,0.0);
		
		if(!matrix.containsKey(term))
			matrix.put(term,new HashMap<Integer,Double>());
		
		Map<Integer,Double> movieTerms = (matrix.get(term).containsKey(movieId) ? matrix.get(term) :temp);
		matrix.get(term).put(movieId, movieTerms.get(movieId)+1.0);
	}
	
	
	/**
	 * @param term - the row id
	 * @param colId - the column id
	 * @return the (Double) element corresponding to (rowId, colId) or null if the element is not present in the matrix
	 */
	public Double getElement(final String term, final Integer colId1)
	{
		
		return (matrix.containsKey(term)) ? matrix.get(term).get(colId1) : null;
	}
	
	
	public Double calculateCosine(Integer Id, Integer id1, ArrayList<String> m1,ArrayList<String> m2)
	{
		double total = 0.0;
		double total2 = 0.0;
		double total1 = 0.0;
		// Loop though each term in m1 doc
		for(String term : m1){
			//If movie 2 contains a term from m1 get the product of there weights(TF-IDF or binary)
			if(m2.contains(term))
				total += matrix.get(term).get(Id)*(matrix.get(term).get(id1));
			//Get the powered sum of all the weights in doc m1
			total2 += Math.pow(matrix.get(term).get(Id),2);
		}
		//Get the powered sum of all the weights in doc m2
		for(String term : m2)
		{
			total1 += Math.pow(matrix.get(term).get(id1),2);
		}
			// Cosine function
		if(total/(Math.sqrt(total2)*Math.sqrt(total1)) < 0)
			System.out.println("negative number");
			
		return total/(Math.sqrt(total2)*Math.sqrt(total1));
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
	
	public void tfIdf()
	{	
		for (Map.Entry<String,Map<Integer,Double>> term : matrix.entrySet()) {
			for (Map.Entry<Integer,Double> movie : term.getValue().entrySet()) {
				double max = maxOccur.get(movie.getKey());
				double size = matrix.get(term.getKey()).size();
				//TF-IDF FORMULA
				double value =(movie.getValue()/max)*(Math.log((1073/(1+Math.abs(size)))));
				matrix.get(term.getKey()).put(movie.getKey(), value);
			}
		}
	}
		

	public void cleanMatrix(Map<Integer, ArrayList<String>> movieWordList) {
		System.out.println("Stop words count before removing terms which only appear once: "+matrix.size());
		ArrayList<String> deleteWords = new ArrayList<String>();
		for (Map.Entry<String,Map<Integer,Double>> term : matrix.entrySet()) {
			//CLEAN MATRIX: loop through all term and if i only appears in one movie add to list
			if(matrix.get(term.getKey()).size()==1){
				deleteWords.add(term.getKey());
			}	
			//MAXOCCuringTerm: loop through all movies (Used in TF-IDF)
			for (Map.Entry<Integer,Double> movie : term.getValue().entrySet()) 
			{
				Double value = (maxOccur.containsKey(movie.getKey()) ? maxOccur.get(movie.getKey()) : 0.0);
				//if the current word count for a movie is greater than the stored replace the stored
				if(movie.getValue() > value)
					maxOccur.put(movie.getKey(), movie.getValue());
			}
			
		}
		//loop over list of words that only appear in one movie and remove these words from both the term-doc matrix and movieWordList array
		for (int i = 0; i < deleteWords.size(); i++) {
			matrix.remove(deleteWords.get(i));
			for (Map.Entry<Integer,ArrayList<String>> term : movieWordList.entrySet()) {
				if(term.getValue().contains(deleteWords.get(i))){
					term.getValue().remove(deleteWords.get(i));
				}
			}
		}
		
		System.out.println("Stop word count after removing terms which appear once: "+matrix.size());
		
	}
	
}
