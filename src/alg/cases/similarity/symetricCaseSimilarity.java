
package alg.cases.similarity;

import alg.cases.Case;
import alg.cases.MovieCase;
import alg.cases.MovieCase1;
import alg.feature.similarity.FeatureSimilarity;

public class symetricCaseSimilarity implements CaseSimilarity
{
	final static double GENRE_WEIGHT = 1.0; // the weight for feature genres
	final static double DIRECTOR_WEIGHT = 1.0; // the weight for feature directors
	final static double ACTOR_WEIGHT = 1.0; // the weight for feature actors
	final static double MEAN_WEIGHT = 1.0;
	final static double POPULARITY_WEIGHT =1.0;
	private double gw =0,dw = 0,aw=0,mw=0,pw=0;
	private double count = 0.0;
	/**
	 * constructor - creates a new OverlapCaseSimilarity object
	 */
	public symetricCaseSimilarity()
	{}
	
	public void printResults(){		
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
				MEAN_WEIGHT* FeatureSimilarity.symetric(m1.getMean(),m2.getMean())+
				+POPULARITY_WEIGHT* FeatureSimilarity.symetric(m1.getPop(),m2.getPop());
		
		double below = GENRE_WEIGHT + DIRECTOR_WEIGHT + ACTOR_WEIGHT + MEAN_WEIGHT+ POPULARITY_WEIGHT;
		return (below > 0) ? above / below : 0;
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
