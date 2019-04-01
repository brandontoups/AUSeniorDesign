package com.seniordesign.titlesearch.test;

import java.util.List;

import com.seniordesign.titlesearch.TitleSearchManager;
import com.seniordesign.titlesearch.WarrantyDeed;

import junit.framework.TestCase;

public class TitleSearchManagerTest extends TestCase {

	public void test100_GetInstanceIsNotNull() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		assert(getInstance != null);
	}

	public void test110_GetHouseHistoryIsValid() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		List<WarrantyDeed> history = getInstance.getHouseHistory();
		assert(history != null);
		assert(history.size() >= 0);
	}

	public void test120_AddWarrantyDeedToHouseHistory() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		List<WarrantyDeed> history = getInstance.getHouseHistory();
		WarrantyDeed wd = new WarrantyDeed();
		getInstance.addWarrantyDeedToHouseHistory(wd);
		assert(history.size() >= 1);
		getInstance.resetHouseHistory();
	}
	
	public void test130_AddWarrantyDeedIsNotNull() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		List<WarrantyDeed> history = getInstance.getHouseHistory();
		WarrantyDeed wd = null;
		getInstance.addWarrantyDeedToHouseHistory(wd);
		assert(history.size() >= 0);
		getInstance.resetHouseHistory();
	}

	public void test140_ResetHouseHistory() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		List<WarrantyDeed> history = getInstance.getHouseHistory();
		WarrantyDeed wd = new WarrantyDeed();
		WarrantyDeed wd1 = new WarrantyDeed();
		WarrantyDeed wd2 = new WarrantyDeed();
		WarrantyDeed wd3 = new WarrantyDeed();
		WarrantyDeed wd4 = new WarrantyDeed();
		getInstance.addWarrantyDeedToHouseHistory(wd);
		getInstance.addWarrantyDeedToHouseHistory(wd1);
		getInstance.addWarrantyDeedToHouseHistory(wd2);
		getInstance.addWarrantyDeedToHouseHistory(wd3);
		getInstance.addWarrantyDeedToHouseHistory(wd4);
		getInstance.resetHouseHistory();
		assert(history != null);
		assert(history.size() == 0);
	}
	
	public void test150_CheckValidateWDReturnsFalseForInvalidBookNumber() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		WarrantyDeed wd = new WarrantyDeed();
		wd.setBookNumber("asdlfkjs");
		wd.setPageNumber("200");
		wd.setParentBookNumber("50");
		wd.setParentPageNumber("400");
		wd.setYearBought("1990");
		wd.setYearSold("2010");
		assert(getInstance.validateWarrantyDeed(wd) == false);
		getInstance.resetHouseHistory();
	}
	
	public void test160_CheckValidateWDReturnsFalseForInvalidPageNumber() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		WarrantyDeed wd = new WarrantyDeed();
		wd.setBookNumber("100");
		wd.setPageNumber("200asdf");
		wd.setParentBookNumber("50");
		wd.setParentPageNumber("400");
		wd.setYearBought("1990");
		wd.setYearSold("2010");
		assert(getInstance.validateWarrantyDeed(wd) == false);
		getInstance.resetHouseHistory();
	}
	
	public void test170_CheckValidateWDReturnsFalseForInvalidParentBookNumber() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		WarrantyDeed wd = new WarrantyDeed();
		wd.setBookNumber("100");
		wd.setPageNumber("200");
		wd.setParentBookNumber("     ");
		wd.setParentPageNumber("400");
		wd.setYearBought("1990");
		wd.setYearSold("2010");
		assert(getInstance.validateWarrantyDeed(wd) == false);
		getInstance.resetHouseHistory();
	}
	
	public void test180_CheckValidateWDReturnsFalseForInvalidParentBookPageNumber() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		WarrantyDeed wd = new WarrantyDeed();
		wd.setBookNumber("100");
		wd.setPageNumber("200");
		wd.setParentBookNumber("50");
		wd.setParentPageNumber("incorrectValue");
		wd.setYearBought("1990");
		wd.setYearSold("2010");
		assert(getInstance.validateWarrantyDeed(wd) == false);
		getInstance.resetHouseHistory();
	}
	
	public void test190_CheckValidateWDReturnsFalseForInvalidYearSold() {
		TitleSearchManager getInstance = TitleSearchManager.getInstance();
		WarrantyDeed wd = new WarrantyDeed();
		wd.setBookNumber("100");
		wd.setPageNumber("200");
		wd.setParentBookNumber("50");
		wd.setParentPageNumber("400");
		wd.setYearBought("12309");
		wd.setYearSold("2010");
		assert(getInstance.validateWarrantyDeed(wd) == false);
		getInstance.resetHouseHistory();
	}
}
