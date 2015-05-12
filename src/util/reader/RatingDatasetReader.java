
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

public class RatingDatasetReader extends DatasetReader
{
	
	/** 
	 * constructor - creates a new DatasetReader object
	 * @param trainFile - the path and filename of the file containing the training user profile data
	 * @param testFile - the path and filename of the file containing the test user profile data
 	 * @param itemFile - the path and filename of the file containing case metadata
	 */
	public RatingDatasetReader(final String trainFile, final String testFile, final String movieFile)
	{
		super(trainFile,testFile,movieFile);
		
	
	}
	
	
	

	/** 
	 * @param filename - the path and filename of the file containing the user profiles
 	 * @return the user profiles
	 */
	public Map<Integer,Map<Integer,Double>> readUserProfiles(final String filename) 
	{

		Map<Integer,Map<Integer,Double>> map = new HashMap<Integer,Map<Integer,Double>>();
		userRatingMap = new HashMap<Integer,ArrayList<Double>>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			br.readLine();// read in header line
			
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
				if(filename.equals("dataset"+File.separator+"trainData.txt")){
					
					userAverageRating(userId,rating);
				}
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
	private void userAverageRating(Integer userId, Double rating) {
		
		ArrayList<Double> scores = new ArrayList<Double>();
		if(!userRatingMap.containsKey(userId)){
		scores.add(0,1.0);
		scores.add(1,rating);
		}
		ArrayList<Double> score = (userRatingMap.containsKey(userId)) ? addRating(userRatingMap.get(userId),rating) : scores;
		userRatingMap.put(userId,score);
	}
	
	private ArrayList<Double> addRating(ArrayList<Double>s,Double r){
		double total =s.get(1)+r;
		double count = s.get(0)+1;
		s.clear();
		s.add(0,count);
		s.add(1,total);
		return s;
	}
	
	
	
	
	
}
