import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;



// Checks the database for the warrantydeed
// calls mina's class if it doesn't exist in database
// populates the WarrantyDeed object from the NLUAPI --> GSON
// jrg0041@auburn.edu - last updated 4/4/2019 @ 4:55pm

public class WarrantyDeedManager {
	
   private String bookNumber;
   private String pageNumber;
   private String parentBookNumber, parentPageNumber;
   private String[] grantors;
   private String[] grantees;
   private String transactionDate;
   private String yearBought;
   private String yearSold;
   private boolean isLatest = false;
   private String text;
   private byte[] pdf;
   
    // Populates the WarrantyDeed to auto-fill the UI fields, should be similar for TrustDeed
    @SuppressWarnings("static-access")
    public WarrantyDeed populateWarrantyDeed(String bookNumber, String pageNumber) {

	// Calls databaseManager class and (hopefully) retrieves a WarrantyDeed
	DatabaseManagerAPI dbManager = new DatabaseManagerAPI();
	WarrantyDeed wd = dbManager.getWarrantyDeed(bookNumber,pageNumber);

	// If deed doesn't exist within database, Calls TitleSearcher class and retrieves a WarrantyDeed
	if (wd == null) {

		TitleSearcher titleSearch = new TitleSearcher();
		wd = titleSearch.getPDFWarrantyDeedBookNo(bookNumber, pageNumber);

	}

	// Call discovery service to fill WarrantyDeed text field
	DiscoveryAPI discoveryAPI = new DiscoveryAPI();
	discoveryAPI.populateWarrantyDeedText(wd);

	// Calls nlu service to fill entity fields (grantors, grantees, bookNumbers, pageNumbers, etc)
	NaturalLanguageUnderstandingAPI nluAPI = new NaturalLanguageUnderstandingAPI();
	@SuppressWarnings("static-access")

	// Populates Deed ------------------------------------------------------------------------------------------------------------>
	AnalysisResults json = nluAPI.analyzeText(wd.getText());

	int size = json.getEntities().size();

	ArrayList<String> tempGrantors = new ArrayList<String>();			// Using ArrayList for dynamic growth 
	ArrayList<String> tempGrantees = new ArrayList<String>();			// (Unsure of how many grantors, grantees, etc)
	ArrayList<String> tempBookNumbers = new ArrayList<String>();		// Holds values for each variable (with possible duplicates)
	ArrayList<String> tempPageNumbers = new ArrayList<String>();


	for (int i = 0; i < size; i++) {									// Stores extracted elements in respective ArrayLists


		// Parsed Grantor(s)
		if (json.getEntities().get(i).getType().equals("Grantor")) {

			tempGrantors.add(json.getEntities().get(i).getText());


		}

		// Parsed Grantee(s)
		if (json.getEntities().get(i).getType().equals("Grantee")) {

			tempGrantees.add(json.getEntities().get(i).getText());

		}

		// Parsed Book Number
		if (json.getEntities().get(i).getType().equals("WarrDeedBookNum")) {

			tempBookNumbers.add(json.getEntities().get(i).getText());

		}

		// Parsed Page Number - may need to be int array for overlapping pages
		if (json.getEntities().get(i).getType().equals("WarrDeedPageNum")) {

			tempPageNumbers.add(json.getEntities().get(i).getText());

		}


	}

	//deletes grantor dupes (by type-conversion)
	Set tempGrantorsSet = new HashSet(tempGrantors);				// converts to HashSet (which doesn't allow duplicates)
	tempGrantors.clear();											// Clears ArrayList (may be collected by garbage collector...)

	List<String> grantorsND = new ArrayList(tempGrantorsSet);		// Converts to List

	//deletes grantee dupes (by type-conversion)
	Set tempGranteesSet = new HashSet(tempGrantees);
	tempGrantees.clear();

	List<String> granteesND = new ArrayList(tempGranteesSet);

	//deletes book number dupes (by type-conversion)
	Set tempBookNumbersSet = new HashSet(tempBookNumbers);
	tempBookNumbers.clear();

	List<String> bookNumbersND = new ArrayList(tempBookNumbersSet);

	//deletes page number dupes (by type-conversion)
	Set tempPageNumbersSet = new HashSet(tempPageNumbers);
	tempPageNumbers.clear();

	List<String> pageNumbersND = new ArrayList(tempPageNumbersSet);

	// fills arrays for grantors, grantees, bookNumbers, pageNumbers, etc
	wd.setGrantors(grantorsND.toArray(new String[grantorsND.size()]));
	wd.setGrantees(granteesND.toArray(new String[granteesND.size()]));
	wd.setBookNumber(bookNumbersND.get(bookNumbersND.size() - 1));
	wd.setPageNumber(pageNumbersND.get(bookNumbersND.size() + 1));



	return wd; 
	}


}
