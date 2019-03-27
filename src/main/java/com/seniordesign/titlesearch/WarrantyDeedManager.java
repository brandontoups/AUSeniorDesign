import java.io.IOException;

import com.google.gson.Gson;

// Checks the database for the warrantydeed
// calls mina's class if it doesn't exist in database
// populates the WarrantyDeed object from the NLUAPI --> GSON
// JRG - last updated 3/26/2019 @ 2:49pm

public class WarrantyDeedManager {

   private String bookNumber, pageNumber;
   private String parentBookNumber, parentPageNumber;
   private String[] grantors;
   private String[] grantees;
   private String transactionDate;
   private String yearBought;
   private String yearSold;
   private boolean isLatest = false;
   private String text;
   private byte[] pdf;

	public WarrantyDeed populateWarrantyDeed(String bookNumber, String pageNumber) {

		// Calls databaseManager class and (hopefully) retrieves a WarrantyDeed
		DatabaseManagerAPI dbManager = new DatabaseManagerAPI();
		WarrantyDeed wd = dbManager.getWarrantyDeed(bookNumber,pageNumber);

		// If deed doesn't exist within database, Calls TitleSearcher class and retrieves a WarrantyDeed
		if (wd == null) {
			TitleSearcherAPI titleSearch = TitleSearcherAPI.getInstance();
			// The '0' parameter refers to Humphreys county, and the
			// '1' field indicates that the time period is before 1993
			wd = titleSearch.getPDFWarrantyDeedBookNo(bookNumber, pageNumber, '0', '1');
			if (wd.getPDF().size() == 0) {
				// The second '0' parameter indicates that time period is after 1993
				wd = titleSearch.getPDFWarrantyDeedBookNo(bookNumber, pageNumber, '0', '0');
			}
		}

		// Warranty deed is not available on TitleSearcher.com
		if (wd.getPDF().size() == 0) {
			throw new FileNotFoundException("Warranty Deed was not found.");
		}

		// Call discovery service to fill WarrantyDeed text field
		DiscoveryAPI discoveryAPI = new DiscoveryAPI();
		discoveryAPI.populateWarrantyDeedText(wd);

		// Calls nlu service to fill entity fields (grantor, grantee)
		NaturalLanguageUnderstandingAPI nluAPI = new NaturalLanguageUnderstandingAPI();
		jsonInString = nluAPI.analyzeText(wd.getText());

		//uses GSON to convert JSON String into Object, uses reflection to store JSON text into WarrantyDeed Object fields
		Gson gson = new Gson();
		wd = gson.fromJson(jsonInString, WarrantyDeed.class);

		return wd;
	}


}
