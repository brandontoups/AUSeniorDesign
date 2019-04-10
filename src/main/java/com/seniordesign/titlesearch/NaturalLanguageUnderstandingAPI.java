package com.seniordesign.titlesearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.MetadataOptions;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

// Connects to NLU API, analyzes the WarrantyDeed text for entities, and returns them.
// JRG - last updated 4/4/2019 @ 5:01pm
public class NaturalLanguageUnderstandingAPI {

	// Variables integral to functionality of NLU service
	  private static final String VERSION = "2018-11-16";
	  private static final String URL = "https://gateway.watsonplatform.net/natural-language-understanding/api/";
	  private static final String API_KEY = "xxx";
	  private static final String MODEL_NO = "xxx";
	  public static AnalysisResults json;
	  public static String[] grantors;
	  public static String[] grantees;
	  private static NaturalLanguageUnderstanding nlu = null;
	  

	// Sets up NLU credentials and undergoes text analysis
	// Parameter should be WarrantyDeed text
	  public static AnalysisResults analyzeText(String str) {
	  
	  // authentication
		IamOptions iamOptions = new IamOptions.Builder()
			.apiKey(API_KEY)
			.build();
		
      // Creates NLU Service and adds credentials
	  	NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstanding(VERSION, iamOptions);
	    	
	    	nlu.setIamCredentials(iamOptions);
	    	nlu.setEndPoint(URL);

    	// Holds the text that we want to analyze --> should be a DYNAMIC variable
    	String html = str;

	    // Entities, keywords, and metadata are parameters we get back from the service about our text

			EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
			  .model(MODEL_NO) // our custom WLK model...
			  .build();

			 KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
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
			

			json = response;
			
			// Print
			//System.out.println(json);
			
			// Return the result (AnalysisResults obj)
			return json;
			
	  }



}

