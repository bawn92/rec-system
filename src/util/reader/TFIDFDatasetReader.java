
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
import util.PorterStemmer;
import util.StopWordRemoval;
import util.TermDocMatrix;

public class TFIDFDatasetReader extends DatasetReader
{
	
	/** 
	 * constructor - creates a new DatasetReader object
	 * @param trainFile - the path and filename of the file containing the training user profile data
	 * @param testFile - the path and filename of the file containing the test user profile data
 	 * @param itemFile - the path and filename of the file containing case metadata
	 */
	public TFIDFDatasetReader(final String trainFile, final String testFile, final String movieFile)
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
				String review = new String(st.nextToken());
				
				//Clean review prior to adding to matrix
				if(filename.equals("dataset"+File.separator+"trainData.txt"))
				{
					addToDocumentTermMatrix(cleanString(review,movieId), movieId);
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
	private void addToDocumentTermMatrix(ArrayList<String> wordString, Integer id) 
	{	
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String>movieTerms = (movieWordList.containsKey(id) ? movieWordList.get(id) :temp);

		for(String term : wordString)
		{
			if(!movieTerms.contains(term))
			{
				movieTerms.add(term);
			}
		}
		//Add each term used in movie to movieWordList
		movieWordList.put(id,movieTerms);
		
		
		if(wordString.size() >= 1)
		{
			for(int i=0; i<wordString.size();i++)
			{
				termDocMatrix.addElement(wordString.get(i),id);		
	
			}
		}
	}
	
	private ArrayList<String> cleanString(String review,Integer id) 
	{	
		ArrayList<String> allWords = new ArrayList<String>();
		PorterStemmer stemmer = new PorterStemmer();
		
		//Clean review
		//1. Remove all non characters 2. Trim whitespace 3. replace all whitespace gaps greater than one with one gap
		//4. Convert to lower case 5. Remove stop Words 6. Split string into array of words 7. Stem each word
		review = review.replaceAll("[^A-Za-z0-9 ]", "").trim();
		review = review.toLowerCase().replaceAll(" +"," ");
		review = StopWordRemoval.removeStopWords(review);
		String[] words = review.split(" ");
		for(int i = 0; i<words.length;i++)
			if(words[i].length()>2)
				allWords.add(StopWordRemoval.stemString(words[i]));
		return allWords;
	}
	
	
	
	
}
