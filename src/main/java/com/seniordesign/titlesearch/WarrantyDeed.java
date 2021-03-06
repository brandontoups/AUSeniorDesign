package com.seniordesign.titlesearch;
/*
 * Warranty Deed Class:
 * Holds the data about an individual Warranty Deed
 * Book #, Page #, Grantor, Grantee, Ownership Chain
 * --------------------------------------------------
 * Obtain JSON Object from NLU-Analyzed text
 * Extract valuable information (Book #, Page #, Names, Price) for getter methods
 * Input corrected values for setter methods
 */

public class WarrantyDeed {

	private String bookNumber = "";
	private String pageNumber = "";
	private String parentBookNumber = "";
	private String parentPageNumber = "";
	private String[] grantors;
	private String[] grantees;
	private String transactionDate = "";
	private String yearBought = "";
	private String yearSold = "";
	private boolean isLatest = false;
	private String text = "";
	private byte[] pdf;
	private String id = "";
	private boolean isValidated = false;

	// Gets warranty deed book #
	public String getBookNumber() {
		return this.bookNumber;
	}

	// Assigns warranty deed book #
	public void setBookNumber(String bookNo) {
		this.bookNumber = bookNo;
		this.updateID();
	}

	// Gets warranty deed page #
	public String getPageNumber() {
		return this.pageNumber;
	}

	// Assigns warranty deed book #
	public void setPageNumber(String pageNo) {
		this.pageNumber = pageNo;
		this.updateID();
	}

	// Gets grantor name
	public String[] getGrantors() {
		return this.grantors;
	}

	// Assigns name to grantor
	public void setGrantors(String[] grantors) {
		this.grantors = grantors;
	}

	// Gets grantee name
	public String[] getGrantees() {
		return this.grantees;
	}

	// Assigns name to grantee
	public void setGrantees(String[] grantees) {
		this.grantees = grantees;
	}

	// Gets warranty deed PDF
	public byte[] getPDF() {
		return this.pdf;
	}

	// Assigns next warranty deed PDF
	public void setPDF(byte[] file) {
		this.pdf = file;
	}

	// Gets warranty deed plaintext
	public String getText() {
		return this.text;
	}

	public void setText(String newText) {
		this.text = newText;
	}

	public String getYearSold() {
		return this.yearSold;
	}

	public void setYearSold(String year) {
		this.yearSold = year;
	}

	public String getYearBought() {
		return this.yearBought;
	}

	public void setYearBought(String year) {
		this.yearBought = year;
	}

	public String getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(String date) {
		this.transactionDate = date;
	}

	public String getParentBookNumber() {
		return this.parentBookNumber;
	}

	public void setParentBookNumber(String bookNo) {
		this.parentBookNumber = bookNo;
		this.updateID();
	}

	public String getParentPageNumber() {
		return this.parentPageNumber;
	}

	public void setParentPageNumber(String pageNo) {
		this.parentPageNumber = pageNo;
		this.updateID();
	}

	public boolean getIsLatest() {
		return this.isLatest;
	}

	public void setIsLatest(boolean value) {
		this.isLatest = value;
	}

	public String getId() {
		return this.id;
	}

	public void updateID() {
		this.id = this.bookNumber + "-" + this.pageNumber + "-" + this.parentBookNumber + "-" + this.parentPageNumber;
	}

	public boolean getIsValidated() {
		return this.isValidated;
	}

	public void setIsValidated(boolean isValid) {
		this.isValidated = isValid;
	}

	public String getID() {
		return this.id;
	}

//   public void main(String[] args) {
//     NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstandingAPI();
//     
//   }

}
