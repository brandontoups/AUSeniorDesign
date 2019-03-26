
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.MetadataOptions;

// Connects to NLU API, analyzes the WarrantyDeed text for entities, and returns them.
// JRG - last updated 3/26/2019 @ 2:49pm
public class NaturalLanguageUnderstandingAPI {

	// Variables integral to functionality of NLU service
	  private static final String VERSION = "2018-11-16";
	  private static final String URL = "https://gateway.watsonplatform.net/natural-language-understanding/api";
	  private static final String USERNAME = "apikey";
	  private static final String API_KEY = "r_nvI1YKmNkdn5O9gLI8RDzqoFzrVoq71gqKlDCkDboe";
	  private static final String MODEL_NO = "b6ac0dd2-42d0-4cfb-b8b9-09080a3df33e";
	  public static String json;
	  private static NaturalLanguageUnderstanding nlu = null;


	// Sets up NLU credentials and undergoes text analysis
	// Parameter should be WarrantyDeed text
	  public static String analyzeText(String str) {

      // Creates NLU Service and adds credentials
	  	NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstanding(VERSION);
	    	nlu.setEndPoint(URL);
	    	nlu.setUsernameAndPassword(USERNAME, API_KEY);

    	// Holds the text that we want to analyze --> should be a DYNAMIC variable
    	String html = str;

	    // Entities, keywords, and metadata are parameters we get back from the service about our text
			MetadataOptions metadata= new MetadataOptions();

			EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
				.model(MODEL_NO) // our custom WLK model...
			  .build();

			KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
			  .limit(5)
			  .build();

			// Specified features that we want to extract
			Features features = new Features.Builder()
			  .metadata(metadata)
			  .entities(entitiesOptions)
			  .keywords(keywordsOptions)
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

			// Stores AnalysisResults object in JSON string
			json = response.toString();

			// Print the result
			System.out.println(json);
			
			return json;
			
	  }



}
