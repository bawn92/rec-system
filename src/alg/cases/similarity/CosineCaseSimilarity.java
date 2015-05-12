package alg.cases.similarity;

import java.util.Map;

import util.OntologyMatrix;
import alg.cases.Case;
import alg.cases.MovieCase;
import alg.feature.similarity.FeatureSimilarity;

public class CosineCaseSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1; // the weight for feature actors
	private OntologyMatrix matrix;
	private Map<String,Integer> genre_count;
	
	/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
	public CosineCaseSimilarity(OntologyMatrix matrix2, Map<String,Integer> gc)  {
		matrix = matrix2;
		genre_count = gc;
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
		
		//Added new similarity function
		double above = GENRE_WEIGHT * FeatureSimilarity.cosine(m1.getGenres(), m2.getGenres(), matrix, genre_count) + 
				DIRECTOR_WEIGHT * FeatureSimilarity.overlap(m1.getDirectors(), m2.getDirectors()) + 
				ACTOR_WEIGHT * FeatureSimilarity.overlap(m1.getActors(), m2.getActors());
		
		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT;
		
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
