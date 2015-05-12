package alg.recommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import util.ScoredThingDsc;
import util.reader.DatasetReader;
import util.reader.PopRatingDatasetReader;
import alg.cases.similarity.CaseSimilarity;

public class CopyOfBoundedGreedyRecommender extends Recommender
{
	private ArrayList<Integer> recommendationIds = new ArrayList<Integer>();
	private int temp111=0;
	// Sortedset to hold movie quality
	private SortedSet<ScoredThingDsc> rrss = new TreeSet<ScoredThingDsc>();
	// recommendation set
	private SortedSet<ScoredThingDsc> r = new TreeSet<ScoredThingDsc>();
	//combination of all r sets
	private SortedSet<ScoredThingDsc> Totalr = new TreeSet<ScoredThingDsc>();
	double k =5;
	double b = 10;
	/**
	 * constructor - creates a new MeanRecommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	public CopyOfBoundedGreedyRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
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
		SortedSet<ScoredThingDsc> Tempss = new TreeSet<ScoredThingDsc>(); 
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
		// iterate over all the target user profile cases and compute a score for the current recommendation candidate case
		for(Integer id: profile.keySet()) 
		{
		ss = new TreeSet<ScoredThingDsc>(); 
		// compute a score for all recommendation candidate cases
		for(Integer candidateId: candidateIds)
		{
			if(!profile.containsKey(candidateId)) // check that the candidate case is not already contained in the user profile
			{
				double score = 0;
					Double sim = super.getCaseSimilarity(candidateId,id);
					if ((sim != null)&&(sim > score)){
						ss.add(new ScoredThingDsc(sim, candidateId)); // add the score for the current recommendation candidate case to the set
					}	
				}
			}
		//To get top b*k cases
		int count =0;
		Tempss = new TreeSet<ScoredThingDsc>(); 
		for(Iterator<ScoredThingDsc> it = ss.iterator(); it.hasNext();)
		{
			//System.out.println(ss.size());
			ScoredThingDsc st = it.next();
			if(count<b*k){
			Tempss.add(st);
			}else{
				break;
			}
			count++;
		}
		ss = Tempss;
		
		r = new TreeSet<ScoredThingDsc>();
		
		// loop for K
		for(int i = 1;i<k;i++){
			rrss = new TreeSet<ScoredThingDsc>();
		//Loop over all C' to produce Quaility	
		for(Iterator<ScoredThingDsc> it = ss.iterator(); it.hasNext(); )
		{
			ScoredThingDsc st = it.next();
			rrss.add(new ScoredThingDsc(Quality(st),st.thing));
		}
		int skipZero = 0;
		// Loop to remove first overrirding .equals wouldnt work
		Tempss = new TreeSet<ScoredThingDsc>();
		for(Iterator<ScoredThingDsc> newIT = rrss.iterator();newIT.hasNext();){
			ScoredThingDsc st = newIT.next();
			if(skipZero!=0){
			Tempss.add(st);
			}
			skipZero ++;
		}
		ss = Tempss;
		r.add(rrss.first());
		}
		//Add all the movie reccomendations for each movie together
		Totalr.addAll(r);		
		}

		// sort the candidate recommendation cases by score (in descending order) and return as recommendations also remove duplicates
		
		for(Iterator<ScoredThingDsc> it = Totalr.iterator(); it.hasNext(); )
		{
			ScoredThingDsc st = it.next();
			if(!recommendationIds.contains((Integer)st.thing))
			recommendationIds.add((Integer)st.thing);
		}
		return recommendationIds;
	}
	
	
	public Double Quality(ScoredThingDsc std){
		
		if(r.isEmpty()){
			return std.score*1.0;
		}
		double runTotal=0.0;
		for(Iterator<ScoredThingDsc> movie = r.iterator(); movie.hasNext(); )
		{
			ScoredThingDsc st = movie.next();
			if (super.getCaseSimilarity((Integer)st.thing,(Integer)std.thing ) != null){
			double temp =super.getCaseSimilarity((Integer)st.thing,(Integer)std.thing );
			runTotal = 1-temp;
			}else{
				runTotal+=1;
				
			}
		}
		return (runTotal/r.size()>0) ? runTotal/r.size()*std.score : 0;
	}	
	
}