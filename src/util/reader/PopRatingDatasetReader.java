
package util.reader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.StringTokenizer;

import alg.casebase.Casebase;
import alg.cases.MovieCase;

public class PopRatingDatasetReader extends DatasetReader
{
	public Map<Integer,Double> movie_pop = new HashMap<Integer,Double>();
	public Map<Integer,ArrayList <Double>> movie_rating = new HashMap<Integer,ArrayList<Double>>();
	/** 
	 * constructor - creates a new DatasetReader object
	 * @param trainFile - the path and filename of the file containing the training user profile data
	 * @param testFile - the path and filename of the file containing the test user profile data
 	 * @param itemFile - the path and filename of the file containing case metadata
	 */
	public PopRatingDatasetReader(final String trainFile, final String testFile, final String movieFile)
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
				
				//String file = "dataset" + File.separator + "trainData.txt";
				if(filename.equals("dataset"+File.separator+"trainData.txt")){
					if (movie_pop == null){
						movie_pop = new HashMap<Integer,Double>();
						movie_pop.put(movieId,1.0);
						}
					else if(movie_pop.containsKey(movieId)){
						movie_pop.put(movieId, movie_pop.get(movieId)+1);
					}
					else{
						movie_pop.put(movieId,1.0);
						}
				
					if (movie_rating == null){
						movie_rating =new HashMap<Integer,ArrayList<Double>>();
						movie_rating.put(movieId,new ArrayList<Double>());
						movie_rating.get(movieId).add(rating);
					}
					else if(movie_rating.containsKey(movieId)){
						movie_rating.get(movieId).add(rating);
					}
					else{
						movie_rating.put(movieId, new ArrayList<Double>());
						movie_rating.get(movieId).add(rating);
					}
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
	
	private Double movie_rating(Integer movieId){
		
		double count=0;
		double total =0;
		for (Double rating : movie_rating.get(movieId)){
			count ++;
			total = total +rating;
			
		}
		
		return total/count;
	}
	
	
	/** 
	 * creates the casebase
	 * @param filename - the path and filename of the file containing the movie metadata
 	 */
	@Override
	public void readCasebase(final String filename) 
	{
		cb = new Casebase();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, "\t");
				if(st.countTokens() != 5)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.out.println(line);
					System.out.println(st.countTokens());
					System.exit(1);
				}
				
				Integer id = new Integer(st.nextToken());
				String title = st.nextToken();
				ArrayList<String> genres = tokenizeString(st.nextToken());
				ArrayList<String> directors = tokenizeString(st.nextToken());
				ArrayList<String> actors = tokenizeString(st.nextToken());
			
				
				
				MovieCase movie = new MovieCase(id, title, genres, directors, actors,movie_rating(id),movie_pop.get(id));
				
				cb.addCase(id, movie);
			}
			System.out.println(cb);
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}
