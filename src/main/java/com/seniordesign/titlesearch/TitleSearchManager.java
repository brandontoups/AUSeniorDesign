package com.seniordesign.titlesearch;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TitleSearchManager {
	private static TitleSearchManager singleInstance = null;
	//private WarrantyDeedManager deedManagerInstance = null;
	private List<WarrantyDeed> houseHistory = new ArrayList<WarrantyDeed>();
	DatabaseManagerAPI database = new DatabaseManagerAPI();
	
	private TitleSearchManager() {
		houseHistory = new ArrayList<WarrantyDeed>();
	}
	
	public static TitleSearchManager getInstance() {
		if(singleInstance == null) {
			singleInstance = new TitleSearchManager();
		}
		return singleInstance;
	}
	
	public List<WarrantyDeed> getHouseHistory() {
		return this.houseHistory;
	}
	
	public void addWarrantyDeedToHouseHistory(WarrantyDeed deed) {
		if(deed != null) {
			try {				
				this.houseHistory.add(deed);
			} catch (Exception e) {
				return;
			}
		}
	}
	
	public void resetHouseHistory() {
		this.houseHistory.clear();
	}
	
	public List<WarrantyDeed> getWarrantyDeed(String bookNo, String pageNo) {
		try {
			Integer.parseInt(bookNo.trim());
			Integer.parseInt(pageNo.trim());
		} catch (NumberFormatException e) {
			return null;
		}
		if(bookNo.isEmpty() || pageNo.isEmpty()) {
			return null;
		}
		WarrantyDeedManager wdManager = new WarrantyDeedManager();
		List<WarrantyDeed> deeds = null;
		try {
			deeds = wdManager.populateWarrantyDeed(bookNo, pageNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return deeds;
	}
	
	public static void main(String[] args) {
		TitleSearchManager manager = TitleSearchManager.getInstance();
		String[] listOfFileNames = new String[] {
				"WD207-1401",
				"WD203-736",
				"WD150-183",
				"WD108-188"
		};
		for(String file : listOfFileNames) {			
			WarrantyDeed deed = manager.createWarrantyDeed(file + ".pdf");
			//deed.setText(manager.getWarrantyDeedText(file + ".txt"));
			manager.addWarrantyDeedToHouseHistory(deed);
		}
//		List<WarrantyDeed> test = manager.getWarrantyDeed("207", "832");
//		System.out.println(test);
//		WarrantyDeed deed2 = test.get(0);
//		manager.addWarrantyDeedToHouseHistory(deed2);
//		byte[] array1 = manager.getHouseHistory().get(0).getPDF();
//		byte[] array2 = deed2.getPDF();
//		System.out.println(array1.length);
//		System.out.println(array2.length);
//		int counter = 0;
//		for(int i = 0; i < array1.length; i++) {
//			if(array1[i] != array2[i]) {
//				counter++;
//			}
//		}
//		System.out.println("Difference count: " + counter);
//		WarrantyDeed deed = new WarrantyDeed();
//		
//		deed.setText("Book WD207 Page 941 BKIPG: WD207/941-942 19000414 III E VALUE 28900.00 0.00 ???_=- TRANSFERTAX 106.93 = RECORDING FEE 10.00 DPFEE 2.00 REGISTER'S FEE 1.00 ? TOTALAMOUNT 119.93 STATE OF TENNESSEE. HUMPHREYS COUNTY THIS INSTRUMENT WAS PREPARED BY Bankers Title & Escrow Corp. THE ACTUAL CONS DERATION OR VALUE, WHICHEVER 3310 West End Ave., Ste. 540 I I NSFERIs $ 28,900.00. Nashville, TN 37203 Af?ant SUBS D AND SWORN TO BEFORE ME, THIS mi 2019. ary Pu-Iai/ c Maiy MY COMMISSION EXPIRESM P19?25619-LW (AFFIX SEAL) WARRANTY DEED ADDRESS NEW OWNER(S) AS FOLLOWS: SEND TAX BILLS TO: MAP-PARCEL NUMBERS Justin Jake Overman and Kailee Ann NEW OWNER PART OF Overman 1600 SW 78th Ave Apt 1215 MAP 014 PARCEL 024.00 Plantation, FL 33324 (CITY) (STATE) (ZIP) (CITY) (STATE) (ZIP) FOR AND CONSIDERATION OF THE SUM OF TEN DOLLARS, CASH IN HAND PAID BY THE HEREINAFTER NAMED GRANTEES, AND OTHER GOOD AND VALUABLE CONSIDERATIONS. THE RECEIPT OF WHICH IS HEREBY ACKNOWLEDGED, Lakewood Ranches, LLC, a Delaware Limited HEREINAFTER CALLED THE GRANTOR, HAS BARGAINED AND SOLD, AND BY THESE PRESENTS DOES TRANSFER AND CONVEY UNTO Justin Jake Overman and Kailee Ann Overman, husband and wife, ? HEREINAFTER CALLED THE GRANTEES, THEIR HEIRS AND ASSIGNS, A CERTAIN TRACT OR PARCEL OF LAND IN HUMPHREYS COUNTY, STATE OF TENNESSEE, DESCRIBED AS FOLLOWS, TO-WIT: Land situated in the First Civil District of Humphreys County, Tennessee, being Lot No. 156 on the Plan of Lakewood Ranches Subdivision (Phase Four Section 2) \"The Ridgecrest \" of record in Plat Book D, Page 126, in the Register's Of?ce for Humphreys County, Tennessee, to which Plan reference is hereby made for a more complete description of the property. Being a portion of the same property conveyed to Lakewood Ranches, LLC, a Delaware Limited Liability Company by Special Warranty deed from Capital Farm Credit, FLCA, a Federally Chartered Corporation of record in Book WD194, page 2864 Register?s Of?ce for Humphreys County , Tennessee, dated May 17, 2010 and recorded on May 21, 2010. Book WD207 Page 942 THIS CONVEYANCE IS SUBJECT TO: (1) Taxes which have been prorated and assumed by Grantee; (2) All restrictions of record; (3) All easements of record; (4) All visible easements; (5) All matters appearing on the plan of record; (6) All applicable governmental and zoning regulations. This is UNIMPROVED property known as Lot No. 156 Lakewood Ranches. Waverly, TN 37185. TO HAVE AND TO HOLD the said tract or parcel of land, with the appurtenances, estate, title and interest thereto belonging to the said GRANTEES, their heirs and assigns forever; and we do covenant with the said GRANTEES that we are lawfully seized and possessed of said land in fee simple, have a good right to convey it and the same is unencumbered, unless otherwise herein set out; and we do ?lrther covenant and bind ourselves, our heirs and representatives, to warrant and forever defend the title to the said land to the said GRANTEES, their heirs and assigns, against the lawful claims of all persons whomsoever. Wherever used, the singular number shall include the Witness Grantor?s hand this the dav of V 2019. TITLE: AYW 4944/ STATE OF m (M U) COUNTY OF git/MW Before me, the undersigned Notary Public in and for the said County and State, personally appeared D0 ?(UL H?A/i It (MA with whom I am personal] ac uainted (or prove to me n the basis of satisfactory evidence), a?d who, upon oath, acknowledged h self to be the e??i? Emmons Development, LLC, Sole Member of Lakewood Ranches, LLC, the within named bargamor, a limited liability company, and that ihe executed the within instrument for the purposes stated therein by signing the name of Emmomve pment, LLC, as Sole Member of Lakewood Ranches, LLC, by MW self as such gent . Witness my hand and of?cial seal this the day of Fab 2019. My Commission expires: a /Z?I \" I WW??Mt/?b Notary Public? 1/ a ? 1-11th . wMAUSSA LOUISE BEVAN STAFFORD } Notary Public- State 01 Florida ?.?-= a 0?; 5%: Q; : Commisston#FF 206091 9? '32, ?533; My Comm Explres Mar 29 2019 ?? ?1 1?0 / tary Assn. at ?W ?535..? Bonded through National No [2 PGSZAL?WARRANTY DEED STELLA BATCH: 47901 02/11/2019 - 10:48 AM, g ._ MORTGAGE TAX, JANET H. DAVIS REGISTER OF DEEDS STATE OF TENNESSEE COUNTY OF DAVIDSON, 2' Z, Liability Company,, plural, the plural the singular, and the use of any gender shall be applicable to all genders., Lakewood Ranches, LLC BY: Emmons Development, LLC, Its Sole Member BY: MOM WW]");
//		deed.setBookNumber("WD202");
//		deed.setPageNumber("1");
//		deed.setYearBought("1998");
//		deed.setIsLatest(true);
//		String[] grantors = {"Jay Patel", "John Doe"};
//		String[] grantees = {"Grantee 1", "Grantee 2"};
//		deed.setGrantors(grantors);
//		deed.setGrantees(grantees);
//		deed.setTransactionDate("2019-02-19");
//		deed.setParentBookNumber("1");
//		deed.setParentPageNumber("1");
//		manager.addWarrantyDeedToHouseHistory(deed);
	}
	
	public List<String> updateWarrantyDeed(int index, WarrantyDeed updatedWD) {
		List<String> errors = validateWarrantyDeed(updatedWD);
		if(errors.isEmpty()) {
			getHouseHistory().remove(index);
			getHouseHistory().add(index, updatedWD);
			database.setWarrantyDeed(updatedWD);
		}
		return errors;
	}
	
	public List<String> validateWarrantyDeed(WarrantyDeed wd) {
		List<String> errors = new ArrayList<String>();
		if(!validateStringRepresentsNumber(wd.getBookNumber())) {
			errors.add("Book number is either missing or invalid.");
		}
		if(!validateStringRepresentsNumber(wd.getPageNumber())) {
			errors.add("Page number is either missing or invalid.");
		}
		if(!wd.getParentBookNumber().isEmpty()) {			
			if(!validateStringRepresentsNumber(wd.getParentBookNumber())) {
				errors.add("Invalid parent book number.");
			}
		}
		if(!wd.getParentPageNumber().isEmpty()) {			
			if(!validateStringRepresentsNumber(wd.getParentPageNumber())) {
				errors.add("Invalid parent book page number.");
			}
		}
		
		if(!wd.getYearSold().isEmpty()) {
			if(!validateStringRepresentsNumber(wd.getYearSold())) {
				errors.add("Year is not a number.");
			} else if(!validateYears(wd.getYearSold())) {
				errors.add("Please enter a valid year.");
			}
		}
		if(!wd.getYearBought().isEmpty()) {			
			if(!validateStringRepresentsNumber(wd.getYearBought())) {
				errors.add("Year is not a number.");
			} else if(!validateYears(wd.getYearBought())) {
				errors.add("Please enter a valid year.");
			}
		}
		return errors;
	}
	
	private boolean validateStringRepresentsNumber(String numberInString) {
		if(numberInString.trim().matches("\\b[0-9]+\\b")) {
			return true;
		}
		return false;
	}
	
	private boolean validateYears(String year) {
		if(year.trim().matches("\\b[0-9]{4}\\b")) {
			return true;
		}
		return false;
	}
	
	public WarrantyDeed createWarrantyDeed(String fileName) {
		WarrantyDeed deed = null;
		String fileFolder = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "warrantyDeedPDFs";
		//String fileFolder = new File("").getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "imageDir";
		System.out.println(fileFolder);
		File file = new File(fileFolder  + File.separator + fileName);
		BufferedInputStream fileContent = null;
		try {
			fileContent = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int bytesToRead;
		try {
			bytesToRead = fileContent.available();
			byte[] pdfInBytes = new byte[bytesToRead];
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			int result;
			while((result = fileContent.read(pdfInBytes)) != -1) {
				stream.write(pdfInBytes, 0, result);
			}
			if(result == -1) {
				deed = new WarrantyDeed();
				deed.setPDF(stream.toByteArray());
				fileContent.close();
				stream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deed;
	}
	
	public String getWarrantyDeedText(String fileName) {
		String text = "";
		
		//String fileFolder = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "warrantyDeedText";
		String fileFolder = new File("").getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "imageDir";
		File file = new File(fileFolder  + File.separator + fileName);
		BufferedInputStream fileContent = null;
		try {
			fileContent = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int bytesToRead;
		try {
			bytesToRead = fileContent.available();
			byte[] textInBytes = new byte[bytesToRead];
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			int result;
			while((result = fileContent.read(textInBytes)) != -1) {
				stream.write(textInBytes, 0, result);
			}
			if(result == -1) {
				text = stream.toString();
				fileContent.close();
				stream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return text;
	}
}
