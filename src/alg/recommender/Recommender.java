
package alg.recommender;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import util.Matrix;
import util.WeightSimMatrix;
import util.reader.DatasetReader;
import util.reader.PopRatingDatasetReader;
import alg.cases.similarity.CaseSimilarity;

public abstract class Recommender 
{
	private Matrix matrix;
	private Matrix matrixO;
	private Matrix matrixG;
	private Matrix matrixD;
	private WeightSimMatrix SimMatrix; // a Matrix object to store case similarities
	private Map<Integer,Double> userGWeights; 
	private Map<Integer,Double> userDWeights; 
	
	/**
	 * constructor - creates a new Recommender object
	 * @param caseSimilarity - an object to compute case similarity
	 * @param reader - an object to store user profile data and movie metadata
	 */
	
	public Recommender(final CaseSimilarity caseSimilarity, final DatasetReader reader)
	{
		// compute all case similarities and store in a Matrix object
		this.matrix = new Matrix();
		Set<Integer> ids = reader.getCasebase().getIds();
		for(Integer rowId: ids)
		{
		//System.out.println(rowId);
			for(Integer colId: ids)
			{
				if(rowId.intValue() != colId.intValue())
				{
					double sim = caseSimilarity.getSimilarity(reader.getCasebase().getCase(rowId), reader.getCasebase().getCase(colId));
					if(sim > 0)
						matrix.addElement(rowId, colId, sim);
				}
			}
		}
	}
	
	
	// New construtor allowing for alterations to the similarity calculations
	public Recommender(final CaseSimilarity caseSimilarity, final DatasetReader reader, Map<Integer,Double> weights)
	{
		
		userDWeights = reader.getUserDWeights();
		userGWeights = weights;
		// compute all case similarities and store in a Matrix object
		this.SimMatrix = new WeightSimMatrix();
		this.matrixO = new Matrix();
		this.matrixG = new Matrix();
		this.matrixD = new Matrix();
		Set<Integer> users = reader.getUserIds();
		Set<Integer> ids = reader.getCasebase().getIds();

			for(Integer rowId: ids){
				
				for(Integer colId: ids){

					if(rowId.intValue() != colId.intValue())
					{
						
						double wSim = caseSimilarity.getGSimilarity(reader.getCasebase().getCase(rowId), reader.getCasebase().getCase(colId));
					
						if(wSim >= 0 )
							matrixG.addElement(rowId, colId, wSim);	
				
						double dSim = caseSimilarity.getDSimilarity(reader.getCasebase().getCase(rowId), reader.getCasebase().getCase(colId));
						if(dSim >= 0 )
							matrixD.addElement(rowId, colId, dSim);	
						
						double oSim = caseSimilarity.getOSimilarity(reader.getCasebase().getCase(rowId), reader.getCasebase().getCase(colId));
					
						if(oSim >= 0)
							matrixO.addElement(rowId,colId,oSim);
					}
				}
				}
	}
	
			
	/**
	 * returns the case similarity between two cases
	 * @param rowId - the id of the first case
	 * @param colId - the id of the second case
	 * @return the case similarity or null if the Matrix object does not contain the case similarity
	 */
	public Double getCaseSimilarity(final Integer rowId, final Integer colId,final Integer userId)
	{
		double o = matrixO.getElement(rowId, colId);
		double g = matrixG.getElement(rowId,colId);
		double d = matrixD.getElement(rowId, colId);
		return (o+(g*userGWeights.get(userId))+(d*userDWeights.get(userId)))/(1+(userGWeights.get(userId)+(userDWeights.get(userId))));
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
