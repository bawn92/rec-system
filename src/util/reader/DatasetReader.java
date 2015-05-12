

package util.reader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

import util.OntologyMatrix;
import util.TermDocMatrix;
import alg.casebase.Casebase;
import alg.cases.MovieCase;


public abstract class DatasetReader 
{
	protected Casebase cb; // stores case objects
	protected Map<Integer,Map<Integer,Double>> userProfiles; // stores training user profiles
	protected Map<Integer,Map<Integer,Double>> testProfiles; // stores test user profiles
	private ArrayList<Integer> ids = new ArrayList<Integer>(); 
	private Map<Integer,ArrayList<Double>> map_test = new HashMap<Integer,ArrayList<Double>>();
	protected Map<Integer,Map<String,Integer>> genreMap = new HashMap<Integer,Map<String,Integer>>();
	protected Map<Integer,Map<String,Integer>> directorMap = new HashMap<Integer,Map<String,Integer>>();
	protected TermDocMatrix termDocMatrix;
	protected Map<Integer,ArrayList<String>> movieWordList = new HashMap<Integer,ArrayList<String>>();
	
	
	//Average user ratings for all movies in user profile
	Map<Integer,ArrayList<Double>> userRatingMap;
	Map<Integer,Double> FinaluserRatingMap = new HashMap<Integer,Double>();
	
	//Used to store Users genre and director weights
	private Map<Integer,Double> userGWeight = new HashMap<Integer,Double>();
	private Map<Integer,Double> userDWeight = new HashMap<Integer,Double>();
	/** 
	 * constructor - creates a new DatasetReader object
	 * @param trainFile - the path and filename of the file containing the training user profile data
	 * @param testFile - the path and filename of the file containing the test user profile data
 	 * @param itemFile - the path and filename of the file containing case metadata
	 */
	public DatasetReader(final String trainFile, final String testFile, final String movieFile)
	{
		termDocMatrix = new TermDocMatrix();
		movieWordList = new HashMap<Integer,ArrayList<String>>();
		userRatingMap = new HashMap<Integer,ArrayList<Double>>();
		userProfiles = readUserProfiles(trainFile);
		//termDocMatrix.cleanMatrix(movieWordList);
		//termDocMatrix.tfIdf();
		testProfiles = readUserProfiles(testFile);
		readCasebase(movieFile);
		
		
		//Follwing methods used to build personilzed weights
		buildUserWeightMap(userProfiles,cb);
		buildUserWeights(userGWeight,genreMap);
		buildUserWeights(userDWeight,directorMap);
		totalUserRatings();
		
	}
	
	private void totalUserRatings() {
		
		for (Map.Entry<Integer,ArrayList<Double>> profile : userRatingMap.entrySet()) {
		
			Double avg_rating  = profile.getValue().get(1)/profile.getValue().get(0);
			FinaluserRatingMap.put(profile.getKey(),avg_rating);
		}
		
	}
	
	
	private void buildUserWeightMap(Map<Integer, Map<Integer, Double>> userProfiles2, Casebase cb2) {
		
		for (Map.Entry<Integer,Map<Integer,Double>> user : userProfiles2.entrySet()) {
			
			for (Map.Entry<Integer,Double> movie : user.getValue().entrySet()) {
				
				//Build map of occunres of each genre or director in each moive of a users profile
				buildGenreMap(cb2.getCase(movie.getKey()).getGenres(),user.getKey());
				buildDirectorMap(cb2.getCase(movie.getKey()).getDirectors(),user.getKey());
			}
			
			
		}
		
	}
	
	private void buildDirectorMap(Set<String> directors, Integer id) {
	
		Map<String,Integer> temp = new HashMap<String,Integer>();
		// If GenreMap dosent contain user ID add
		if(!directorMap.containsKey(id)){
			directorMap.put(id, new HashMap<String,Integer>());
		}
		
		Iterator<String> iter = directors.iterator();
		while (iter.hasNext()){
			String director = iter.next().toString();
			temp = new HashMap<String,Integer>();
			// If user hashMap dosent contain genre add
			if(!directorMap.get(id).containsKey(director)){
				temp =directorMap.get(id);
				temp.put(director, 1);
				directorMap.put(id, temp);
			}else{
				temp=directorMap.get(id);
				temp.put(director,directorMap.get(id).get(director)+1);
				directorMap.put(id,temp);
			}
			
		}
		
		
	}

	private void buildUserWeights(Map<Integer,Double> userWeight, Map<Integer,Map<String,Integer>> userMap) {
		
		for (Map.Entry<Integer,Map<String,Integer>> user : userMap.entrySet()) {
			Double totalFeatures = 0.0;
			Double DistinctFeatures = 0.0;
			Double weight =0.0;
			for (Map.Entry<String,Integer> genre : user.getValue().entrySet()) {
				totalFeatures += genre.getValue();
				DistinctFeatures++;
			}
			weight = 1-((DistinctFeatures-(1.0))/(totalFeatures));
			userWeight.put(user.getKey(),weight);
		}
		
	}
	
	private void buildGenreMap(Set<String> genres,Integer id) {
		Map<String,Integer> temp = new HashMap<String,Integer>();
		// If GenreMap dosent contain user ID add
		if(!genreMap.containsKey(id)){
			genreMap.put(id, new HashMap<String,Integer>());
		}
		Iterator<String> iter = genres.iterator();
		while (iter.hasNext()){
			String genre = iter.next().toString();
			temp = new HashMap<String,Integer>();
			// If user hashMap dosent contain genre add
			if(!genreMap.get(id).containsKey(genre)){
				temp =genreMap.get(id);
				temp.put(genre, 1);
				genreMap.put(id, temp);
			}else{
				temp=genreMap.get(id);
				temp.put(genre,genreMap.get(id).get(genre)+1);
				genreMap.put(id,temp);
			}	
		}
		
	}

	/** 
 	 * @return the training user profile ids
	 */
	public int getSize()
	{
		return userProfiles.size();
	
	}
	public Set<Integer> getUserIds()
	{
		return userProfiles.keySet();
	}
	
	public int getSize1(){
		
		
		return ids.size();
	}
	
	public Map<Integer,ArrayList<Double>> getmap(){
		
		return map_test;
		
	}

	/** 
	 * @param id - the id of the training user profile to return
 	 * @return the training user profile
	 */
	public Map<Integer,Double> getUserProfile(final Integer id)
	{
		return userProfiles.get(id);
	}
	
	/** 
 	 * @return the test user profile ids
	 */
	public Set<Integer> getTestIds()
	{
		return testProfiles.keySet();
	}
	
	/** 
	 * @param id - the id of the test user profile to return
 	 * @return the test user profile
	 */
	public Map<Integer,Double> getTestProfile(final Integer id)
	{
		return testProfiles.get(id);
	}
	
	/**
	 * @return the casebase
	 */
	public Casebase getCasebase()
	{
		return cb;
	}
	
	/** 
	 * @param filename - the path and filename of the file containing the user profiles
 	 * @return the user profiles
	 */
	public abstract Map<Integer,Map<Integer,Double>> readUserProfiles(final String filename);
	
	
	/** 
	 * creates the casebase
	 * @param filename - the path and filename of the file containing the movie metadata
 	 */
	public void readCasebase(final String filename){
		
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
				
					//countgenres(genres);
					
					MovieCase movie = new MovieCase(id, title, genres, directors, actors);
					
					cb.addCase(id, movie);
				}
				
				br.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		

	/** 
	 * @param str - the string to be tokenized; ',' is the delimiter character
 	 * @return a list of string tokens
	 */
	public ArrayList<String> tokenizeString(final String str)
	{
		ArrayList<String> al = new ArrayList<String>();
		
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
			al.add(st.nextToken().trim()); 
		
		return al;
	}

	public OntologyMatrix getMatrix(){ 
		return null;
		}

	public Map<String,Integer> getGenreCount() {
		return null;
	}

	public Map<Integer,Double> getUserGWeights() {
		
		return userGWeight;
	}
	
	public Map<Integer,Double> getUserDWeights() {
		
		return userDWeight;
	}

	public Map<Integer,Double> getUserRatings() {
		
		return FinaluserRatingMap;
	}
	
	public TermDocMatrix gettermDocMatrix(){
		
		return termDocMatrix;
		
	}
	
	public Map<Integer,ArrayList<String>> getMovieWordList(){
		
		return movieWordList;
		
	}

}

	
