/*
 * Warranty Deed Class:
 * Holds the data about an individual Warranty Deed
 * Book #, Page #, Grantor, Grantee, Ownership Chain
 * --------------------------------------------------
 * Obtain JSON Object from NLU-Analyzed text
 * Extract valuable information (Book #, Page #, Names, Price) for getter methods
 * Input corrected values for setter methods
 * jrg0041@auburn.edu - last updated 4/4/2019 @ 4:55pm
 */

 public class WarrantyDeed {
	 
	   public static String bookNumber;
	   public static String pageNumber;
	   public static String parentBookNumber;
	   public static String parentPageNumber;
	   public static String[] grantors;
	   public static String[] grantees;
	   public static String transactionDate;
	   public static String yearBought;
	   public static String yearSold;
	   public static boolean isLatest;
	   public static String text;
	   public static byte[] pdf;
	   public static String id;
	 
  // Constructor
  public WarrantyDeed() {

	   String bookNumber = "Cannot Find";
	   String pageNumber = "Cannot Find";
	   String parentBookNumber = "Cannot Find";
	   String parentPageNumber = "Cannot Find";
	   String[] grantors = null;
	   String[] grantees = null;
	   String transactionDate = "Cannot Find";
	   String yearBought = "Cannot Find";
	   String yearSold = "Cannot Find";
	   boolean isLatest = false;
	   String text = "Cannot Find";
	   byte[] pdf = null;
	   String id = "Cannot Find";

  }
	 
   // Gets warranty deed book #
   public String getBookNumber() {
     return this.bookNumber;
   }

   // Assigns warranty deed book #
   public void setBookNumber(String bookNo) {
	   this.bookNumber = bookNumber;
	   updateID();
   }

   // Gets warranty deed page #
   public String getPageNumber() {
     return this.pageNumber;
   }

   // Assigns warranty deed book #
   public void setPageNumber(String pageNo) {
	   this.pageNumber = pageNumber;
	   updateID();
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
	   updateID();
   }
   
   public String getParentPageNumber() {
	   return this.parentPageNumber;
   }
   
   public void setParentPageNumber(String pageNo) {
	   this.parentPageNumber = pageNo;
	   updateID();
   }
   
   public boolean getIsLatest() {
	   return this.isLatest;
   }
   
   public void setIsLatest(boolean value) {
	   this.isLatest = value;
   }
   
   public String getID() {
	   return this.id;
   }
   
   public void updateID() {
	   id = this.bookNumber + "-" + this.pageNumber + "-" + this.parentBookNumber + "-" + this.parentPageNumber;
   }


 }
