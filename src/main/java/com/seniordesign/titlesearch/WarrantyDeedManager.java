import java.io.IOException;

import com.google.gson.Gson;

// Checks the database for the warrantydeed
// Calls Mina's class if it doesn't exist in database
// Populates the WarrantyDeed object from the NLUAPI --> GSON
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

	public List<WarrantyDeed> populateWarrantyDeed(String bookNumber, String pageNumber) {

		// Calls DatabaseManager class and (hopefully) retrieves a WarrantyDeed
		DatabaseManagerAPI dbManager = new DatabaseManagerAPI();
		List<WarrantyDeed> wd = new ArrayList<WarrantyDeed>();
    List<WarrantyDeed> wdFinal = new ArrayList<WarrantyDeed>();
    wd = dbManager.getWarrantyDeed(bookNumber, pageNumber);

		// If deed doesn't exist within database, calls TitleSearcher class and retrieves a WarrantyDeed
		if (wd.size() == 0) {
			TitleSearcherAPI titleSearch = TitleSearcherAPI.getInstance();
			// The '0' parameter refers to Humphreys county, and the
			// '1' field indicates that the time period is before 1993
			wd = titleSearch.getPDFWarrantyDeed(bookNumber, pageNumber, '0', '1');
			if (wd.size() == 0) {
				// The second '0' parameter indicates that time period is after 1993
				wd = titleSearch.getPDFWarrantyDeed(bookNumber, pageNumber, '0', '0');
        // Warranty deed is not available on TitleSearcher.com
        if (wd.size() == 0) {
          throw new FileNotFoundException("Warranty Deeds were not found.");
        }
			}
		}

    for (int counter = 0; counter < wd.size(); counter++)
    {
      WarrantyDeed warrantyDeed = wd.get(counter);
      // Call discovery service to fill WarrantyDeed text field
      DiscoveryAPI discoveryAPI = new DiscoveryAPI();
      discoveryAPI.populateWarrantyDeedText(warrantyDeed);

      // Calls NLU service to fill entity fields (grantor, grantee)
      NaturalLanguageUnderstandingAPI nluAPI = new NaturalLanguageUnderstandingAPI();
      jsonInString = nluAPI.analyzeText(warrantyDeed.getText());

      // Uses GSON to convert JSON String into Object, uses reflection to store JSON text into WarrantyDeed Object fields
      Gson gson = new Gson();
      warrantyDeed = gson.fromJson(jsonInString, WarrantyDeed.class);
      wdFinal.add(warrantyDeed);
    }
		return wdFinal;
	}

}
