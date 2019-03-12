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

   public String bookNumber, pageNumber;
   public String primaryGrantor, pimaryGrantee;
   private String text;
   private byte[] pdf;

//   // WarrantyDeed Constructor
//   public WarrantyDeed(String bookNumber, int pageNumber, String pimaryGrantor, String primaryGrantee, String warrantyDeedText) {
//
//     this.bookNumber = bookNumber;
//     this.primaryGrantor = primaryGrantor;
//     this.primaryGrantee = primaryGrantee;
//     this.pageNumber = pageNumber;
//     this.warrantyDeedText = warrantyDeedText;
//   }

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
   public String getGrantor() {
     return this.primaryGrantor;
   }

   // Assigns name to grantor
   public void setGrantor(String grantor) {

   }

   // Gets grantee name
   public String getGrantee() {
     return this.primaryGrantor;
   }

   // Assigns name to grantee
   public void setGrantee(String grantee) {

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

//   public void main(String[] args) {
//     NaturalLanguageUnderstanding nlu = new NaturalLanguageUnderstandingAPI();
//     
//   }

 }
