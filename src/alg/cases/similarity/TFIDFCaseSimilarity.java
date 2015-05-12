package alg.cases.similarity;

import java.util.ArrayList;
import java.util.Map;

import util.TermDocMatrix;
import alg.cases.Case;
import alg.cases.MovieCase;
import alg.feature.similarity.FeatureSimilarity;

public class TFIDFCaseSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1; // the weight for feature actors
	final static double REVIEW_WEIGHT = 1;
	
	private TermDocMatrix tdm;
	private Map<Integer,ArrayList<String>> movieWordList;
	/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 * @param termDocMatrix 
	 * @param mtl 
	 */
	public TFIDFCaseSimilarity(TermDocMatrix termDocMatrix, Map<Integer,ArrayList<String>> mtl)
	{
		tdm = termDocMatrix;
		movieWordList = mtl;
	}
	
	/**
	 * computes the similarity between two cases
	 * @param c1 - the first case
	 * @param c2 - the second case
	 * @return the similarity between case c1 and case c2
	 */
	public double getSimilarity(final Case c1, final Case c2) 
	{
		MovieCase m1 = (MovieCase)c1;
		MovieCase m2 = (MovieCase)c2;
		
		double above = GENRE_WEIGHT * FeatureSimilarity.overlap(m1.getGenres(), m2.getGenres()) + 
				DIRECTOR_WEIGHT * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) + 
				ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors())+
				REVIEW_WEIGHT * FeatureSimilarity.tfIdf(m1.getId(),m2.getId(),tdm,movieWordList.get(m1.getId()),movieWordList.get(m2.getId()));
		
		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT+REVIEW_WEIGHT;
		
		return (below > 0) ? above / below : 0;
	}

	@Override
	public void printResults() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getOSimilarity(Case c1, Case c2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDSimilarity(Case c1, Case c2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getGSimilarity(Case c1, Case c2) {
		// TODO Auto-generated method stub
		return 0;
	}
}
