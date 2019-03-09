package com.seniordesign.titlesearch;

import java.util.ArrayList;
import java.util.List;

public class TitleSearchManager {
	private static TitleSearchManager singleInstance = null;
	private List<WarrantyDeed> houseOwnershipHistory;
	
	private TitleSearchManager() {
		houseOwnershipHistory = new ArrayList<WarrantyDeed>();
	}
	
	public TitleSearchManager getInstance() {
		if(singleInstance == null) {
			singleInstance = new TitleSearchManager();
		}
		return singleInstance;
	}
	
	public WarrantyDeed analyzeTextWithNLU(WarrantyDeed deed) {
		
		return deed;
	}
}
