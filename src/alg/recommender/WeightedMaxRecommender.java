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

public class WeightedMaxRecommender extends Recommender
{
	/**
	 * constructor - creates a new MeanRecommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public WeightedMaxRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader, Map<Integer,Double> weights)
	{
		super(caseSimilarity, reader, weights);
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
			if(!profile.containsKey(candidateId)) // check that the candidate case is not already contained in the user profile
			{
				double score = 0;
				// iterate over all the target user profile cases and compute a score for the current recommendation candidate case
				for(Integer id: profile.keySet()) 
				{
					Double sim = super.getCaseSimilarity(candidateId, id,userId);
					if ((sim != null)&&(sim > score)){
						sim.doubleValue();
						score = sim;
						//score += (sim != null) ? sim.doubleValue() : 0;
					}
				}				

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
}
