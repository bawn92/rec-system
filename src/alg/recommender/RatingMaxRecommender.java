package alg.recommender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import util.ScoredThingDsc;
import util.reader.DatasetReader;
import util.reader.PopRatingDatasetReader;
import alg.cases.similarity.CaseSimilarity;

public class RatingMaxRecommender extends RatingRecommender
{
	/**
	 * constructor - creates a new MeanRecommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public RatingMaxRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
	{
		super(caseSimilarity, reader);
	}
	
	/**
	 * returns a ranked list of recommended case ids
	 * @param userId - the id of the target user
	 * @param reader - an object to store user profile data and movie metadata
	 * @return the ranked list of recommended case ids
	 */
	public ArrayList<Integer> getRecommendations(final Integer userId, final DatasetReader reader)
	{
		SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); 
		// get the target user profile
		Map<Integer,Double> profile = reader.getUserProfile(userId);
		Map<Integer,Double> userRatings = reader.getUserRatings();

		// get the ids of all recommendation candidate cases
		Set<Integer> candidateIds = reader.getCasebase().getIds();
		if(profile == null){
			System.out.println(userId);
			System.out.println("HERE");
		}
		
		if(candidateIds == null){
			System.out.println("CANDIATE");
		}
		// compute a score for all recommendation candidate cases
		for(Integer candidateId: candidateIds)
		{
			double totalTop = 0.0,totalBottom =0.0;
			if(!profile.containsKey(candidateId)) // check that the candidate case is not already contained in the user profile
			{
				double score = 0;
				// iterate over all the target user profile cases and compute a score for the current recommendation candidate case
				for(Integer id: profile.keySet()) 
				{
					Double sim = super.getCaseSimilarity(candidateId, id);
					if (sim != null){
						totalTop += sim*normalize(userRatings.get(userId));
						totalBottom += Math.abs(sim);
					}
				}				
				score = totalTop/totalBottom;
				score = denormalize(score);
				if(score > 0)
					ss.add(new ScoredThingDsc(score, candidateId)); // add the score for the current recommendation candidate case to the set
			}
				
		}

		// sort the candidate recommendation cases by score (in descending order) and return as recommendations 
		ArrayList<Integer> recommendationIds = new ArrayList<Integer>();
		
		for(Iterator<ScoredThingDsc> it = ss.iterator(); it.hasNext(); )
		{
			ScoredThingDsc st = it.next();
			recommendationIds.add((Integer)st.thing);
		}
		return recommendationIds;
	}

	private double denormalize(double score) {
		return (0.5*((score+1)*(5-0.5)))+0.5;
		
	}

	private Double normalize(Double double1) {
		return ((2*(double1-0.5))-(5-0.5))/(5-0.5);
	}
}
