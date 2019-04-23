package com.seniordesign.titlesearch;

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
}
