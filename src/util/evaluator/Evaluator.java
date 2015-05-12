package util.evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.reader.DatasetReader;
import util.reader.PopRatingDatasetReader;
import alg.recommender.RatingRecommender;
import alg.recommender.Recommender;

public class Evaluator 
{
	private Map<Integer,ArrayList<Integer>> recommendations; // stores the ranked list of recommended case ids for each test user
	private DatasetReader reader; // stores user profile data and movie metadata
	private boolean boll = true;
	private int count =0;
	/**
	 * constructor - creates a new Evaluator object
	 * @param recommender - an object to define a case-based recommender
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public Evaluator(final Recommender recommender, final DatasetReader reader)
	{
		recommendations = new HashMap<Integer,ArrayList<Integer>>();
		this.reader = reader;
		
		int counter = 0;
		for(Integer userId: reader.getTestIds())
		{
			if(++counter %10 == 0) System.out.print(".");
			//System.out.println(userId+","+recommender.getRecommendations(userId, reader).size());
			recommendations.put(userId, recommender.getRecommendations(userId, reader));
		}
		System.out.println();
	}
	
	public Evaluator(final RatingRecommender recommender, final DatasetReader reader)
	{
		recommendations = new HashMap<Integer,ArrayList<Integer>>();
		this.reader = reader;
		
		int counter = 0;
		for(Integer userId: reader.getTestIds())
		{
			if(++counter %10 == 0) System.out.print(".");
			//System.out.println(userId+","+recommender.getRecommendations(userId, reader).size());
			recommendations.put(userId, recommender.getRecommendations(userId, reader));
		}
		System.out.println();
	}
	
	/**
	 * computes the mean precision (over all test users) for a given recommendation list size provided by the case-based recommender
	 * @param topN - the size of the recommendation list
	 * @return the precision @ topN
	 */
	public double getPrecision(final int topN)
	{
		double sum = 0;
		
		for(Integer userId: recommendations.keySet())
		{	
			int size = getIntersection(recommendations.get(userId), reader.getTestProfile(userId), topN, userId).size();
			sum += (topN > 0) ? size * 1.0 / topN : 0;
		}
		
		return (recommendations.keySet().size() > 0) ? sum / recommendations.keySet().size() : 0;
	}

	/**
	 * computes the mean recall (over all test users) for a given recommendation list size provided by the case-based recommender
	 * @param topN - the size of the recommendation list
	 * @return the recall @ topN
	 */
	public double getRecall(final int topN)
	{
		double sum = 0;
		
		for(Integer userId: recommendations.keySet())
		{	
			int size = getIntersection(recommendations.get(userId), reader.getTestProfile(userId), topN,userId).size();
			sum += (reader.getTestProfile(userId).size() > 0) ? size * 1.0 / reader.getTestProfile(userId).size() : 0;
		}
		
		return (recommendations.keySet().size() > 0) ? sum / recommendations.keySet().size() : 0;
	}
	
	/**
	 * gets the intersection between two sets
	 * @param recs - the ranked list of recommended case ids
	 * @param testProfile - the user test profile
	 * @param topN - the size of the recommendation list
	 * @return the intersection between two sets
	 */
	private ArrayList<Integer> getIntersection(final ArrayList<Integer> recs, final Map<Integer,Double> testProfile, final int topN, final int userId)
	{
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for(int i = 0; i < recs.size() && i < topN; i++)
		{
			if(boll){
			if(userId == 170827209){
			//	System.out.println(recs.get(i));
				if(count ==10){
				boll=false;}
				count++;
			}
			}
			Integer id = recs.get(i);
			if(testProfile.containsKey(id))
				intersection.add(id);
			
		}

		return intersection;
	}	
}
