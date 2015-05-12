
package util.reader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.StringTokenizer;

import alg.casebase.Casebase;
import alg.cases.MovieCase;

public class WeightedFiliterDatasetReader extends DatasetReader
{
	
	private Map<Integer,ArrayList<Double>> user_mean = new HashMap<Integer,ArrayList<Double>>();
	private ArrayList<Integer> outBoundsUser1 = new ArrayList<Integer>();
	private ArrayList<Integer> inBoundsUser = new ArrayList<Integer>();
	private Map<Integer,ArrayList<Integer>> remove_users = new HashMap<Integer,ArrayList<Integer>>();
	/** 
	 * constructor - creates a new DatasetReader object
	 * @param trainFile - the path and filename of the file containing the training user profile data
	 * @param testFile - the path and filename of the file containing the test user profile data
 	 * @param itemFile - the path and filename of the file containing case metadata
	 */
	public WeightedFiliterDatasetReader(final String trainFile, final String testFile, final String movieFile)
	{
		super(trainFile,testFile,movieFile);
		generateMean_SD();
		filterProfiles();
		//update_profiles();
		//System.out.println("Here");
	}
	

	private void filterProfiles() {
		
		for (Map.Entry<Integer,Map<Integer,Double>> profile : userProfiles.entrySet()) {
			int out =0;
			/*for (Map.Entry<Integer,Double> movies : profile.getValue().entrySet()) {
				 if(movies.getValue()>=personilzed_weight(profile.getKey())){
					 out++;
					}else{
						
						userProfiles.get(profile.getKey()).remove(movies.getKey());
					}
				}*/
			Iterator<Map.Entry<Integer,Double>> iter = profile.getValue().entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<Integer,Double> entry = iter.next();
			    if(entry.getValue()>=personilzed_weight(profile.getKey())){
					 out++;
					}else{
			        iter.remove();
			    }
			}
			
			
			
			if(out ==0){
				if(!inBoundsUser.contains(profile.getKey())){
					inBoundsUser.add(profile.getKey());
				}
			}
		}
			
		for (Map.Entry<Integer,Map<Integer,Double>> profile : testProfiles.entrySet()) {
			//System.out.println(profile.getKey());
			int temp =0;int out =0;
			/*for (Map.Entry<Integer,Double> movie : profile.getValue().entrySet()) {
				temp++;
				 if(movie.getValue()>=personilzed_weight(profile.getKey())){
					 out++;
					}else{
						
						testProfiles.get(profile.getKey()).remove(movie.getKey());
					}
				}*/
			
			Iterator<Map.Entry<Integer,Double>> iter = profile.getValue().entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<Integer,Double> entry = iter.next();
			    if(entry.getValue()>=personilzed_weight(profile.getKey())){
					 out++;
					}else{
			        iter.remove();
			    }
			}
			
			if(out ==0){
				if(!inBoundsUser.contains(profile.getKey())){
					inBoundsUser.add(profile.getKey());
				}
			}
		}
		System.out.println(inBoundsUser.size());
		for(int i=0; i<inBoundsUser.size();i++){
			userProfiles.remove(inBoundsUser.get(i));
			testProfiles.remove(inBoundsUser.get(i));
		}
	}	

	/*
	private void update_profiles() {
		for(int i=0;i<inBoundsUser.size();i++){
			if(!userProfiles.containsKey(outBoundsUser.get(i)) || !testProfiles.containsKey(outBoundsUser.get(i))){
				System.out.println("remove");
				userProfiles.remove(i);
				testProfiles.remove(i);
				if(!outBoundsUser1.contains(i)){
					System.out.println("hereoutbounds");
					outBoundsUser1.add(i);
				}
			}
		}
	}*/


	public void generateMean_SD(){
		
		ArrayList<Double> movie_ratings = new ArrayList<Double>();
		double total = 0.0, mean = 0.0, sd = 0.0; movie_ratings = new ArrayList<Double>();
		for (Map.Entry<Integer,Map<Integer,Double>> profile : userProfiles.entrySet()) {
			total=0.0;mean=0.0;sd=0.0;movie_ratings = new ArrayList<Double>();
			for (Map.Entry<Integer,Double> movies : profile.getValue().entrySet()) {
				movie_ratings.add(movies.getValue());
				total = total + movies.getValue(); 
			}
			mean = total/movie_ratings.size();
			total=0.0;
			for (int i=0; i<movie_ratings.size();i++){
				total = total + Math.pow(movie_ratings.get(i)-mean,2);
			}
			sd = total/movie_ratings.size();
			ArrayList<Double> temp = new ArrayList<Double>();
			temp.add(mean);
			temp.add(sd);
			user_mean.put(profile.getKey(),temp);
		}
		
		
	}
	
	
	/** 
	 * @param filename - the path and filename of the file containing the user profiles
 	 * @return the user profiles
	 */
	public Map<Integer,Map<Integer,Double>> readUserProfiles(final String filename) 
	{

		Map<Integer,Map<Integer,Double>> map = new HashMap<Integer,Map<Integer,Double>>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			br.readLine(); // read in header line
			
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, "\t");
				if(st.countTokens() != 4)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}
				
				Integer userId = new Integer(st.nextToken());
				Integer movieId = new Integer(st.nextToken());
				Double rating = new Double(st.nextToken());
				
			
				Map<Integer,Double> profile = (map.containsKey(userId) ? map.get(userId) : new HashMap<Integer,Double>());
				profile.put(movieId, rating);
				map.put(userId, profile);
			}
			
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		return map;
	}
	
	private Double personilzed_weight(Integer userId) {
		ArrayList<Double>details = user_mean.get(userId);
		return Math.min(4.5,(details.get(0))+((0.5)*details.get(1)));
	}	
	


	
}
