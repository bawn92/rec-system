package alg.cases.similarity;

import java.util.Map;

import alg.cases.Case;
import alg.cases.MovieCase;
import alg.feature.similarity.FeatureSimilarity;

public class FeatureWeightCaseSimilarity implements CaseSimilarity
{
	static double GENRE_WEIGHT; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1; // the weight for feature actors
		/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
	public FeatureWeightCaseSimilarity()
	{
		
		
	}
	
	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	@Override
	public void printResults() {
		// TODO Auto-generated method stub
		
	}
	
	//Return each feature sim seperaptly in order to apply correct weighted

	//Get Orginal Similarity, which already has weight applied
	public double getOSimilarity(Case c1, Case c2) {
		MovieCase m1 = (MovieCase)c1;
		MovieCase m2 = (MovieCase)c2;
		double above =
				ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors());
				
		if (above < 0)
			System.out.println("realy wtf");
		return above;
	}
	
	//Get Director Similarity
	public double getDSimilarity(Case c1, Case c2) {
		MovieCase m1 = (MovieCase)c1;
		MovieCase m2 = (MovieCase)c2;
		//System.out.println(weight);
		double above =  FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors());	

		if (above < 0)
			System.out.println("realy wtf");
		return above;
	}
	
	// Get Genre Similarity
	public double getGSimilarity(Case c1, Case c2) {
		MovieCase m1 = (MovieCase)c1;
		MovieCase m2 = (MovieCase)c2;
		//System.out.println(weight);
		double above =  FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres());	

		if (above < 0)
			System.out.println("realy wtf");
		return above;
	}

	public double getSimilarity(Case c1, Case c2){
		return 0.0;
	}
	
}
