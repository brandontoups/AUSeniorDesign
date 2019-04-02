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
	
	public WarrantyDeed getWarrantyDeed(String bookNo, String pageNo) {
		try {
			Integer.parseInt(bookNo);
			Integer.parseInt(pageNo);
		} catch (NumberFormatException e) {
			return null;
		}
		if(bookNo.isEmpty() || pageNo.isEmpty()) {
			return null;
		}
		WarrantyDeedManager wdManager = new WarrantyDeedManager();
		WarrantyDeed deed = null;
		try {
			deed = wdManager.populateWarrantyDeed(bookNo, pageNo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deed;
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
	}
	
	public void updateWarrantyDeed(int index, WarrantyDeed updatedWD) {
		boolean isDeedValidForUpdate = validateWarrantyDeed(updatedWD);
		if(isDeedValidForUpdate) {
			getHouseHistory().add(index, updatedWD);
		}
	}
	
	public boolean validateWarrantyDeed(WarrantyDeed wd) {
		List<Boolean> testResults = new ArrayList<Boolean>();
		testResults.add(validateStringRepresentsNumber(wd.getBookNumber().trim()));
		testResults.add(validateStringRepresentsNumber(wd.getPageNumber().trim()));
		testResults.add(validateStringRepresentsNumber(wd.getParentBookNumber().trim()));
		testResults.add(validateStringRepresentsNumber(wd.getParentPageNumber().trim()));
		if(!wd.getYearSold().isEmpty()) {
			testResults.add(validateStringRepresentsNumber(wd.getYearSold().trim()));
		}
		if(!wd.getYearBought().isEmpty()) {			
			testResults.add(validateStringRepresentsNumber(wd.getYearBought().trim()));
		}
		testResults.add(validateYears(wd.getYearSold().trim()));
		testResults.add(validateYears(wd.getYearBought().trim()));
		for(Boolean value : testResults) {
			if(value.booleanValue() == false) {
				return false;
			}
		}
		return true;
	}
	
	private boolean validateStringRepresentsNumber(String numberInString) {
		if(numberInString.matches("\\b[0-9]+\\b")) {
			return true;
		}
		return false;
	}
	
	private boolean validateYears(String year) {
		if(year.matches("\\b[0-9]{4}\\b")) {
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
