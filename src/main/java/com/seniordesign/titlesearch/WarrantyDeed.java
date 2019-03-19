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
	 
   public static void assignRoles(AnalysisResults json) {
   // Stores entities from NaturalLanguageUnderstanding class in iterable list (from JSON)
   	java.util.List<EntitiesResult> entityList = response.getEntities();

   // Get specific text element from list and assign to object member variable -- TEMP/STC!!
	grantors[0] = entityList.get(0).getText();
	grantors[1] = entityList.get(1).getText();
	grantees[0] = entityList.get(2).getText();
	grantees[1] = entityList.get(3).getText(); 

  }

   // Gets warranty deed book #
   public String getBookNumber() {
     return this.bookNumber;
   }

   // Assigns warranty deed book #
   public void setBookNumber(String bookNo) {
	   this.bookNumber = bookNo;
   }

   // Gets warranty deed page #
   public String getPageNumber() {
     return this.pageNumber;
   }

   // Assigns warranty deed book #
   public void setPageNumber(String pageNo) {
	   this.pageNumber = pageNo;
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
   }
   
   public String getParentPageNumber() {
	   return this.parentPageNumber;
   }
   
   public void setParentPageNumber(String pageNo) {
	   this.parentPageNumber = pageNo;
   }
   
   public boolean getIsLatest() {
	   return this.isLatest;
   }
   
   public void setIsLatest(boolean value) {
	   this.isLatest = value;
   }

//   public void main(String[] args) {
//     NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstandingAPI();
//     
//   }

 }
