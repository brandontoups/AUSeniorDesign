import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.MetadataOptions;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

public class nluAPI {

	// Version
	private static final String VERSION = "2018-11-16";
	// Upon creation of NLU service instance, you are given apikey and URL. This is apikey
	private static final String API_KEY = "p42yaEOtuDzcJzOXOy4uSM_Mnq9VnW1k6LL4jYm1cEtb";
	// This is the URL.
	private static final String URL = "https://gateway.watsonplatform.net/natural-language-understanding/api/";
	private static final String MODEL_NO = "2c925630-0a35-4c32-8f55-cde46579e2b6";
	public static String json;


	// Sets up NLU credentials and undergoes text analysis
	// Parameter should be WarrantyDeed text
	public static String analyzeText() {

		// auth
		IamOptions iamOptions = new IamOptions.Builder()
				.apiKey(API_KEY) // correct
				.build();
		
		// Creates NLU Service and adds credentials
		NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstanding(VERSION, iamOptions);
		nlu.setIamCredentials(iamOptions);
		nlu.setEndPoint(URL);	    

		The text we want to analyze. You can insert any other text you like.
	    String bpText = "thence with this lot South 3' 30' West 75 feet to " +
	        "a stake in the north boundary line of the said alley, said stake " + 
	    	"being 3 feet West of the center of a light pole; thence with the " + 
		    "said alley North 86??30' West 125 feet to the beginning., Being the " +
		    "same property conveyed to Patti J. Bowles by deed of record in Deed" +
		    "Book 157, page 197, Register's Of???ce of Humphreys County, Tennessee.," +
		    "FOR AND IN CONSIDERATION or THE sum OF ONE AND NO/lDO DOLLAR " + 
			"($1.00) cash, and other good and valuable considerations, Now, therefore, " +
			"WE, JIMMY DWIGHT McCORD and wife, SANDRA LEE McCORD, have bargained, and sold, " +
			"and by these presents does transfer and convey unto the said WILLIAM HOWARD MULLINIKS, " +
			"his heirs and representatives, a certain tract or parcel of land lying in the Third Civil " +
			"District of Humphreys County, Tennessee, as follows";
	    
	     //Entities and keywords are parameters you get back from the service about your text.
	     EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
	    		 .model(MODEL_NO)
			     .build();

	     KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
			     .build();

	     Features features = new Features.Builder()
	    		 .entities(entitiesOptions)
	    		 .build();

	     AnalyzeOptions parameters = new AnalyzeOptions.Builder()
	    		 .text(bpText)
	    		 //.text(ggText)
	    		 .features(features)
	    		 .build();
	     
	     // Take the parameters and send them to your service for results.
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

