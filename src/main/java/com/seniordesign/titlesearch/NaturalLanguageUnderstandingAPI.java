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
	
	// Variables integral to functionality of NLU service
	  private static final String VERSION = "2018-11-16";
	  private static final String URL = "https://gateway.watsonplatform.net/natural-language-understanding/api";
	  private static final String USERNAME = "apikey";
	  private static final String API_KEY = "xxx";
	  private static final String MODEL_NO = "xxx";
	  private static NaturalLanguageUnderstanding nlu;
   
	  // Variables related to Warranty Deed (Grantor, Grantee, Book#, etc) 
	  private static String[] grantors;
	  private static String[] grantees;
	  private static String bookNumber;
	  private static String warrantyDeedText;
	  private static int pageNumber;
	  private static byte[] pdf;
	  
	  /* Constructor (use as needed)
	  public NaturalLanguageUnderstandingAPI(String primaryGrantor, String primaryGrantee, String bookNumber, int pageNumber, String warrantyDeedText, byte[] pdf) {
		  
		  this.primaryGrantor = primaryGrantor;
		  this.primaryGrantee = primaryGrantee;
		  this.bookNumber = bookNumber;
		  this.pageNumber = pageNumber;
		  this.warrantyDeedText = warrantyDeedText;
		  this.pdf = pdf;
		  
	  }
	  */
	  
	  // Sets up NLU credentials and Builders for text analysis
	  public static NaturalLanguageUnderstanding analyzeText(String str) {
	    // Creates NLU Service and adds credentials
	  	NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstanding(VERSION);
	    	nlu.setEndPoint(URL);
	    	nlu.setUsernameAndPassword(USERNAME, API_KEY);
	    	
	    	// Holds the text that we want to analyze --> should be a DYNAMIC variable 
	    	String html = str;
	    	nlu = analyzeText(html);

	    	// Entities, keywords, and metadata are parameters we get back from the service about our text
			MetadataOptions metadata= new MetadataOptions();

			EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
			  .model(MODEL_NO)
			  .build();
			
		// Specified features that we want to extract
			Features features = new Features.Builder()
			  .metadata(metadata)
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

		// Print the result
			System.out.println(response);
			
		// Store results in list, iterate through list capturing sought-after elements
			java.util.List<EntitiesResult> entityList = response.getEntities();
			java.util.List<KeywordsResult> keywordList = response.getKeywords();
			
		// Get specific text element from list and assign to object member variable
			grantors[0] = entityList.get(0).getText();
			grantors[1] = entityList.get(1).getText();
			grantees[0] = entityList.get(2).getText();
			grantees[1] = entityList.get(3).getText();
			bookNumber = keywordList[0].getText();
			warrantyDeedText = str; 
			

			
			/* Prints out roles --> Only needed for testing 
			 * System.out.println("Primary Grantor: " + primaryGrantor);
			 * System.out.println("Secondary Grantor: " + secondaryGrantor); 
			 * System.out.println("Primary Grantee: " + primaryGrantee);
			 * System.out.println("Book Number: " + bookNumber);
			 * System.out.println("Warranty Deed Text: " + warrantyDeedText);
			 */
			
			

			return nlu;
	  }
	  
}

// Create list of JSON values, call getText() and getRelevance() to iterate thru list and pick out needed elements 
// Assign needed elements to String variables, retrieve String variables from NLU API with WarrantyDeed Class and store them
// Create response object, store bookNumber, grantors, grantees, etc --> send to WarrantyDeedManager Class
// Demo where analyzed text appears on UI after uploading PDF
