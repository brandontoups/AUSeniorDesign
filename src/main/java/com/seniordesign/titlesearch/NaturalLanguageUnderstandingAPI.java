package com.seniordesign.titlesearch;
//those import give us access to the classes inside the jar file we used.
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.MetadataOptions; 


public class NaturalLanguageUnderstandingAPI {
	
	public class NaturalLanguageUnderstandingAPI {
	
	// Variables integral to functionality of NLU service
	  private static final String VERSION = "2018-11-16";
	  private static final String URL = "https://gateway.watsonplatform.net/natural-language-understanding/api";
	  private static final String USERNAME = "apikey";
	  private static final String API_KEY = "xxx";
	  private static final String MODEL_NO = "xxx";
	  private static NaturalLanguageUnderstanding nlu = null;
   
	  // Variables related to Warranty Deed (Grantor, Grantee, Book#, etc) 
	  private static String[] grantors;
	  private static String[] grantees;
	  private static String bookNumber, pageNumber;
	  private static String text;
	  private static int pageNumber;
	  private static byte[] pdf;
	  
	  
	  // Sets up NLU credentials and Builders for text analysis, connects to NLU service
	  private NaturalLanguageUnderstanding connectToNLU() {
	    
		// Creates NLU Service and adds credentials
	  	NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstanding(VERSION);
	    	nlu.setEndPoint(URL);
	    	nlu.setUsernameAndPassword(USERNAME, API_KEY);
	    	return nlu;
	  }
	  
	  public static NaturalLanguageUnderstanding analyzeText(String str) {
	    	// Holds the text that we want to analyze --> should be DYNAMIC
	    	String html = str;

	    	// Entities are the only parameters we get back from the service about our text
			EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
			  .model(MODEL_NO)
			  .build();
			
		// Specified features that we want to extract
			Features features = new Features.Builder()
			  .entities(entitiesOptions)
			  .build();

		// Analyzes the specified features
			AnalyzeOptions parameters = new AnalyzeOptions.Builder()
			  .html(html)
			  .features(features)
			  .build();

		// Takes the parameters and sends them to nlu service for results
			AnalysisResults response = nlu
			  .analyze(parameters)
			  .execute();
	  	
		  return nlu;	
	  }
	  
}
