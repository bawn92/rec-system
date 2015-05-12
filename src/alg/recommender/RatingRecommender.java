package alg.recommender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Matrix;
import util.WeightSimMatrix;
import util.reader.DatasetReader;
import util.reader.PopRatingDatasetReader;
import alg.cases.similarity.CaseSimilarity;

public abstract class RatingRecommender 
{
	private Matrix matrix;
	private Matrix matrixO;
	private Matrix matrixG;
	private Matrix matrixD;
	private WeightSimMatrix SimMatrix; // a Matrix object to store case similarities
	private Map<Integer,Double> userGWeights; 
	private Map<Integer,Double> userDWeights; 
	Map<Integer,Double> userRatings;
	
	/**
	 * constructor - creates a new Recommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	
	public RatingRecommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
	{
		// compute all case similarities and store in a Matrix object
		this.matrix = new Matrix();
		Set<Integer> userIds = reader.getUserIds();
		Set<Integer> ids = reader.getCasebase().getIds();
		userRatings = reader.getUserRatings();
		for(Integer rowId: ids){
			double sim = 0.0;
			for(Integer colId: ids){
				Double totalTop =0.0,totalBottom = 0.0,totalBottom1 = 0.0;
				if(rowId.intValue() != colId.intValue())
				{
					for(Integer id: userIds){
						Map<Integer,Double> profile = reader.getUserProfile(id);
						if(profile.containsKey(colId) && profile.containsKey(rowId)){
							//System.out.println(profile.get(colId));
							//System.out.println(profile.get(rowId));
							//System.out.println(userRatings.get(id));
							totalTop += (profile.get(colId)-userRatings.get(id))*(profile.get(rowId)-userRatings.get(id));
							totalBottom += Math.pow((profile.get(colId)-userRatings.get(id)), 2);
							totalBottom1 += Math.pow((profile.get(rowId)-userRatings.get(id)), 2);
						}
					}
				}
				sim = (totalTop ==0 && totalBottom == 0 && totalBottom1 ==0) ? 0 : (totalTop)/((Math.sqrt(totalBottom))*(Math.sqrt(totalBottom1))) ;
					matrix.addElement(rowId, colId, sim);
			}
		}
	}
	
	/**
	 * returns a ranked list of recommended case ids
	 * @param userId - the id of the target user
	 * @param reader - an object to store user profile data and movie metadata
	 * @return the ranked list of recommended case ids
	 */
	public abstract ArrayList<Integer> getRecommendations(final Integer userId, final DatasetReader reader);


	public Double getCaseSimilarity(final Integer rowId, final Integer colId)
	{
		return matrix.getElement(rowId, colId);
	}

	public Double getRatingCaseSimilarity(Integer candidateId, Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
