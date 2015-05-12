package alg.feature.similarity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import util.Matrix;
import util.OntologyMatrix;
import util.TermDocMatrix;

public class FeatureSimilarity 
{
	/**
	 * computes exact match similarity between string feature values
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between string feature values
	 */
	public static double exact(final String s1, final String s2)
	{
		if(s1.trim().compareToIgnoreCase(s2.trim()) == 0) return 1.0;
		else return 0.0;
	}
	
	/**
	 * computes overlap similarity between set feature values
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between set feature values
	 */
	public static double overlap(final Set<String> s1, final Set<String> s2) 
	{
		int intersection = 0;
		
		for(String str: s1)
			if(s2.contains(str))
				intersection++;
		
		int min = (s1.size() < s2.size()) ? s1.size() : s2.size();		
		return (min > 0) ? intersection * 1.0 / min : 0;
	}
	
	/**
	 * computes Jaccard similarity between set feature values
	 * @param s1 - the first feature value
	 * @param s2 - the second feature value
	 * @return the similarity between set feature values
	 */	
	public static double Jaccard(final Set<String> s1, final Set<String> s2) 
	{
		int intersection = 0;
		
		for(String str: s1)
			if(s2.contains(str))
				intersection++;
		
		int union = s1.size() + s2.size() - intersection;		
		return (union > 0) ? intersection * 1.0 / union : 0;
	}
	
	
	public static double cosine(final Set<String> s1, final Set<String> s2, OntologyMatrix matrix, Map<String,Integer> gc){
		double total = 0.0;
		// if genres are identical use overlap 
		double overlap = overlap(s1,s2);
		if (overlap == 1.0)
			return 1.0;
		
		// loop through s1
		for(String genre : s1){
			// loop through s2
			for(String genreB : s2){
				if(s2.contains(genre)){
					total +=1.0;
					break;
					}
				// Occurances of pair <a,b> divided by the max occurances of either genre
				//System.out.println("Here");
				double bottom = (Math.max(gc.get(genre)*1.0,gc.get(genreB)*1.0));
				double top =matrix.getElement(genre,genreB);
				total += top/bottom;
			}
			
		}
		double sim = total/Math.max(s1.size()*1.0,s2.size()*1.0); 
		return (sim > 0) ? sim : 0;
	}

	
	public static double cosineEdited(final Set<String> s1, final Set<String> s2, OntologyMatrix matrix, Map<String,Integer> gc){
		double total = 0.0;
		// if genres are identical use overlap 
		double overlap = overlap(s1,s2);
		if (overlap == 1.0)
			return 1.0;
		// loop through s1
		for(String genre : s1){
			// loop through s2
			for(String genreB : s2){
				if(s2.contains(genre)){
					total +=1.0;
					break;
					}else{
				// Occurances of pair <a,b> divided by the max occurances of either genre
				double bottom = (Math.max(gc.get(genre)*1.0,gc.get(genreB)*1.0));
				double top =matrix.getElement(genre,genreB);
				total += top/bottom;}
			}
		}
		double sim = total/(s1.size()*1.0+s2.size()*1.0)-total; 
		return (sim > 0) ? sim : 0;
	}
	
	public static double symetric(Double meanA, Double meanB){
		double abs = 1.0-Math.abs(meanA-meanB)/meanA;
		return (abs > 0) ? abs : 0;
	}
	
	public static double asymetric(Double meanA, Double meanB){
		double abs = 1.0 - (Math.abs(meanA-meanB)/meanA +Math.max(0,meanA- meanB) ) ;
		return (abs>0) ? abs : 0;
	}

	public static double tfIdf(Integer integer, Integer integer2, TermDocMatrix tdm, ArrayList<String> movieWordList, ArrayList<String> movieWordList1) {		
		return tdm.calculateCosine(integer,integer2,movieWordList,movieWordList1);
	}
	
	
}
