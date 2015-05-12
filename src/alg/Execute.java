package alg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import alg.casebase.Casebase;
import alg.cases.similarity.CaseSimilarity;
import alg.cases.similarity.CosineCaseSimilarity;
import alg.cases.similarity.FeatureWeightCaseSimilarity;
import alg.cases.similarity.JaccardCaseSimilarity;
import alg.cases.similarity.OverlapCaseSimilarity;
import alg.cases.similarity.TFIDFCaseSimilarity;
import alg.cases.similarity.asymetricCaseSimilarity;
import alg.cases.similarity.symetricCaseSimilarity;
import alg.recommender.BoundedGreedyRecommender;
import alg.recommender.MaxRecommender;
import alg.recommender.RatingMaxRecommender;
import alg.recommender.RatingRecommender;
import alg.recommender.Recommender;
import alg.recommender.MeanRecommender;
import alg.recommender.WeightedMaxRecommender;
import util.TermDocMatrix;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;
import util.reader.FiliterDatasetReader;
import util.reader.OntologyDatasetReader;
import util.reader.OrginalDatasetReader;
import util.reader.PopRatingDatasetReader;
import util.reader.RatingDatasetReader;
import util.reader.TFIDFDatasetReader;
import util.reader.WeightedFeatureDatasetReader;
import util.reader.WeightedFiliterDatasetReader;

public class Execute 
{
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException
	{	
		// set the paths and filenames of the training, test and movie metadata files and read in the data
		String trainFile = "dataset" + File.separator + "trainData.txt";
		String testFile = "dataset" + File.separator + "testData.txt";
		String movieFile = "dataset" + File.separator + "movies.txt";
		
		//Task 2
		//DatasetReader reader = new OrginalDatasetReader(trainFile, testFile, movieFile);
	//	CaseSimilarity caseSimilarity = new OverlapCaseSimilarity();
		//Recommender recommender = new MaxRecommender(caseSimilarity, reader);
		
		
		// configure the case-based recommendation algorithm - set the case similarity and recommender
		//CaseSimilarity caseSimilarity = new OverlapCaseSimilarity();
		//Recommender recommender = new MaxRecommender(caseSimilarity, reader);
		//N.B for accessing the CosineCaseSimiliarty please use the following lines of code:
		//DatasetReader reader = new OntologyDatasetReader(trainFile, testFile, movieFile);
		//CaseSimilarity caseSimilarity = new CosineCaseSimilarity(reader.getMatrix(),reader.getGenreCount());
		
		//Use for task 7
		DatasetReader reader = new WeightedFeatureDatasetReader(trainFile, testFile, movieFile);
		CaseSimilarity caseSimilarity = new FeatureWeightCaseSimilarity();
		Recommender recommender = new WeightedMaxRecommender(caseSimilarity, reader, reader.getUserDWeights());
		
		
		//Use for task 8
		//DatasetReader reader = new RatingDatasetReader(trainFile, testFile, movieFile);
		//CaseSimilarity caseSimilarity = new FeatureWeightCaseSimilarity();
		//RatingRecommender recommender = new RatingMaxRecommender(caseSimilarity, reader);
		
		//Use for task 9 run binary by commenting out line 61 in dataset reader
		//DatasetReader reader = new TFIDFDatasetReader(trainFile, testFile, movieFile);
		//CaseSimilarity caseSimilarity = new TFIDFCaseSimilarity(reader.gettermDocMatrix(),reader.getMovieWordList());
		//Recommender recommender = new MaxRecommender(caseSimilarity, reader);
		
		
		
		//System.out.println("here");
		//System.out.println(total/weights.size());
		//Recommender recommender = new WeightedMaxRecommender(caseSimilarity, reader, reader.getUserGWeights());
		
		
		// evaluate the case-based recommender
		Evaluator eval = new Evaluator(recommender, reader);
		
		System.out.println("topN Recall Precision");
		for(int topN = 5; topN <= 50; topN += 5){
		double f1 = (2*(eval.getRecall(topN)*eval.getPrecision(topN)))/(eval.getRecall(topN)+eval.getPrecision(topN));
		System.out.println(topN + " " + eval.getRecall(topN) + " " + eval.getPrecision(topN)+ " " +f1);
		}
		  
		
	}
}
