

package util.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.StringTokenizer;

import util.OntologyMatrix;
import alg.casebase.Casebase;
import alg.cases.MovieCase;

public class OntologyDatasetReader extends DatasetReader
{
	//Genre count used in getting the Max occurance of either genre in the pair. Used in Similarity function 
	private Map<String,Integer> genre_count;
	// A matrix storing the occurences of each pair
	private OntologyMatrix matrix;
	// A list of the all the genre names, used to update matrix with pairs it dosent contain
	private ArrayList<String> allGenres;
	
	/** 
	 * constructor - creates a new DatasetReader object
	 * @param trainFile - the path and filename of the file containing the training user profile data
	 * @param testFile - the path and filename of the file containing the test user profile data
 	 * @param itemFile - the path and filename of the file containing case metadata
	 */
	public OntologyDatasetReader(final String trainFile, final String testFile, final String movieFile)
	{
		super(trainFile,testFile,movieFile);
		//Cleans Matrix , For pairs that do not appear_set_to_zero eg: if <a,b> is not present add <a,b,0>
		matrix.addZeros(allGenres);
	}
	
	
	/** 
	 * @param filename - the path and filename of the file containing the user profiles
 	 * @return the user profiles
	 */
	
	//Read  User profile not edited
	public Map<Integer,Map<Integer,Double>> readUserProfiles(final String filename) 
	{

		Map<Integer,Map<Integer,Double>> map = new HashMap<Integer,Map<Integer,Double>>();
		
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
	
	@Override
	public void readCasebase(final String filename) 
	{
		cb = new Casebase();
		
		// count of each genre apperance
		genre_count = new HashMap<String,Integer>();
		//Initalizing matrix and AllGenres
		matrix = new OntologyMatrix();
		allGenres = new ArrayList<String>();
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
			
				// pass the genres in the matrix as they are read from files
				buildGenreMatrix(genres);
				
				MovieCase movie = new MovieCase(id, title, genres, directors, actors);
				
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
	
	public void buildGenreMatrix(ArrayList<String> genres){
		// find all unique pair in list and pass to matrix.add
		for (int i =0; i<genres.size(); i++){
			//Add genre to allGenres if not already there
			if(!allGenres.contains(genres.get(i)))
				allGenres.add(genres.get(i));
			//counts how many times each genre appears
			if( genre_count.containsKey(genres.get(i))){
				genre_count.replace(genres.get(i), genre_count.get(genres.get(i))+1);
			}else{
				genre_count.put(genres.get(i),1);
			}
			
			// loops through each combination of genres and adds to matrix
			for (int x =i+1; x<genres.size(); x++){
				matrix.addElement(genres.get(i), genres.get(x));
			}
		}
	}
	
	//Allows for the retriving of generated matrix and genre count

	@Override
	public OntologyMatrix getMatrix(){
		return matrix;
	}
	
	@Override
	public Map<String,Integer> getGenreCount(){
			return genre_count;
	}
	
}
