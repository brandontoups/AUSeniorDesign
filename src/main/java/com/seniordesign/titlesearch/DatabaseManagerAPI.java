package com.seniordesign.titlesearch;



/*
 * This file will contain the get functions for the warranty and trust deeds
 */
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;


import com.google.gson.Gson;

@ApplicationPath("api")
@Path("/WarrantyDeed")
public class DatabaseManagerAPI extends Application {
	DatabaseManagerStore store = DatabaseManagerStoreInstance.getInstance();
	
	
	/*
	 * gets the warrantyDeed based on the book and page number
	 * @return the warrantyDeed
	 */
//	@GET
//	@Path("/")
//	@Produces("application/json")
	public WarrantyDeed getWarrantyDeed(String bookNumber, String pageNumber) {
		WarrantyDeed wd = new WarrantyDeed();
		
		//List<String> bookNum = new ArrayList<String>();
		for (WarrantyDeed deed : store.getAll()) {
			System.out.println(deed);
			//We know that we have bookNumber in the parameter, so we first need to get 
			//the bookNumer from the list and then compare it
			String bookNumber1 = deed.getBookNumber();
			if(bookNumber.equals(bookNumber1)) {
				//List<String> pageNum = new ArrayList<String>();
				String pageNumber1 = deed.getPageNumber();
				if(pageNumber.equals(pageNumber1)) {
					wd = deed;
					
					break;
					
				}
			}
			else {
				wd = null;
			}
		}
		return wd;
	}
	
	/**
	 * Returns a warranty deed document based on the id string
	 * @param id
	 * @return
	 */
	
	public WarrantyDeed getWarrantyDeed1(String id) {
		WarrantyDeed wd = new WarrantyDeed();
		for (WarrantyDeed deed : store.getAll()) {
			String ids = deed.getID();
			if(id.equals(ids)) {
				wd = deed;
			
				break;
			}
			else {
				wd = null;
			}
		}
		
		return wd;
	}
	
	/**
	 * Returns a warranty deed based on the grantors
	 * @param name
	 * @return
	 */
	public WarrantyDeed getWarrantyDeed2(String name) {
		WarrantyDeed wd = new WarrantyDeed();
		for (WarrantyDeed deed : store.getAll()) {
			String[] names = deed.getGrantors();
			for(int i = 0; i < names.length; i++) {
				if(name.equals(names[i])) {
					wd = deed;
					System.out.println("Hi");
					break;
				}
				else {
					wd = null;
				}
			}
		}
		return wd;
	}
	/*
	 * takes in a warrantyDeed object and passes it on to the database
	 */
	public WarrantyDeed setWarrantyDeed(WarrantyDeed wd) {
		store.store(wd);
		return wd;
	}
	
	
	/**
	 * Testing function for the moment
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseManagerStore store = DatabaseManagerStoreInstance.getInstance();
		
		WarrantyDeed deed = new WarrantyDeed();
		
		deed.setText("Book WD207 Page 941 BKIPG: WD207/941-942 19000414 III E VALUE 28900.00 0.00 ???_=- TRANSFERTAX 106.93 = RECORDING FEE 10.00 DPFEE 2.00 REGISTER'S FEE 1.00 ? TOTALAMOUNT 119.93 STATE OF TENNESSEE. HUMPHREYS COUNTY THIS INSTRUMENT WAS PREPARED BY Bankers Title & Escrow Corp. THE ACTUAL CONS DERATION OR VALUE, WHICHEVER 3310 West End Ave., Ste. 540 I I NSFERIs $ 28,900.00. Nashville, TN 37203 Af?ant SUBS D AND SWORN TO BEFORE ME, THIS mi 2019. ary Pu-Iai/ c Maiy MY COMMISSION EXPIRESM P19?25619-LW (AFFIX SEAL) WARRANTY DEED ADDRESS NEW OWNER(S) AS FOLLOWS: SEND TAX BILLS TO: MAP-PARCEL NUMBERS Justin Jake Overman and Kailee Ann NEW OWNER PART OF Overman 1600 SW 78th Ave Apt 1215 MAP 014 PARCEL 024.00 Plantation, FL 33324 (CITY) (STATE) (ZIP) (CITY) (STATE) (ZIP) FOR AND CONSIDERATION OF THE SUM OF TEN DOLLARS, CASH IN HAND PAID BY THE HEREINAFTER NAMED GRANTEES, AND OTHER GOOD AND VALUABLE CONSIDERATIONS. THE RECEIPT OF WHICH IS HEREBY ACKNOWLEDGED, Lakewood Ranches, LLC, a Delaware Limited HEREINAFTER CALLED THE GRANTOR, HAS BARGAINED AND SOLD, AND BY THESE PRESENTS DOES TRANSFER AND CONVEY UNTO Justin Jake Overman and Kailee Ann Overman, husband and wife, ? HEREINAFTER CALLED THE GRANTEES, THEIR HEIRS AND ASSIGNS, A CERTAIN TRACT OR PARCEL OF LAND IN HUMPHREYS COUNTY, STATE OF TENNESSEE, DESCRIBED AS FOLLOWS, TO-WIT: Land situated in the First Civil District of Humphreys County, Tennessee, being Lot No. 156 on the Plan of Lakewood Ranches Subdivision (Phase Four Section 2) \"The Ridgecrest \" of record in Plat Book D, Page 126, in the Register's Of?ce for Humphreys County, Tennessee, to which Plan reference is hereby made for a more complete description of the property. Being a portion of the same property conveyed to Lakewood Ranches, LLC, a Delaware Limited Liability Company by Special Warranty deed from Capital Farm Credit, FLCA, a Federally Chartered Corporation of record in Book WD194, page 2864 Register?s Of?ce for Humphreys County , Tennessee, dated May 17, 2010 and recorded on May 21, 2010. Book WD207 Page 942 THIS CONVEYANCE IS SUBJECT TO: (1) Taxes which have been prorated and assumed by Grantee; (2) All restrictions of record; (3) All easements of record; (4) All visible easements; (5) All matters appearing on the plan of record; (6) All applicable governmental and zoning regulations. This is UNIMPROVED property known as Lot No. 156 Lakewood Ranches. Waverly, TN 37185. TO HAVE AND TO HOLD the said tract or parcel of land, with the appurtenances, estate, title and interest thereto belonging to the said GRANTEES, their heirs and assigns forever; and we do covenant with the said GRANTEES that we are lawfully seized and possessed of said land in fee simple, have a good right to convey it and the same is unencumbered, unless otherwise herein set out; and we do ?lrther covenant and bind ourselves, our heirs and representatives, to warrant and forever defend the title to the said land to the said GRANTEES, their heirs and assigns, against the lawful claims of all persons whomsoever. Wherever used, the singular number shall include the Witness Grantor?s hand this the dav of V 2019. TITLE: AYW 4944/ STATE OF m (M U) COUNTY OF git/MW Before me, the undersigned Notary Public in and for the said County and State, personally appeared D0 ?(UL H?A/i It (MA with whom I am personal] ac uainted (or prove to me n the basis of satisfactory evidence), a?d who, upon oath, acknowledged h self to be the e??i? Emmons Development, LLC, Sole Member of Lakewood Ranches, LLC, the within named bargamor, a limited liability company, and that ihe executed the within instrument for the purposes stated therein by signing the name of Emmomve pment, LLC, as Sole Member of Lakewood Ranches, LLC, by MW self as such gent . Witness my hand and of?cial seal this the day of Fab 2019. My Commission expires: a /Z?I \" I WW??Mt/?b Notary Public? 1/ a ? 1-11th . wMAUSSA LOUISE BEVAN STAFFORD } Notary Public- State 01 Florida ?.?-= a 0?; 5%: Q; : Commisston#FF 206091 9? '32, ?533; My Comm Explres Mar 29 2019 ?? ?1 1?0 / tary Assn. at ?W ?535..? Bonded through National No [2 PGSZAL?WARRANTY DEED STELLA BATCH: 47901 02/11/2019 - 10:48 AM, g ._ MORTGAGE TAX, JANET H. DAVIS REGISTER OF DEEDS STATE OF TENNESSEE COUNTY OF DAVIDSON, 2' Z, Liability Company,, plural, the plural the singular, and the use of any gender shall be applicable to all genders., Lakewood Ranches, LLC BY: Emmons Development, LLC, Its Sole Member BY: MOM WW]");
		deed.setBookNumber("2");
		deed.setPageNumber("1");
		deed.setYearBought("1998");
		deed.setIsLatest(true);
		String[] grantors = {"Jay Patel", "John Doe"};
		String[] grantees = {"Grantee 1", "Grantee 2"};
		deed.setGrantors(grantors);
		deed.setGrantees(grantees);
		deed.setTransactionDate("2019-02-19");
		deed.setParentBookNumber("1");
		deed.setParentPageNumber("1");
		
	
		DatabaseManagerAPI api = new DatabaseManagerAPI();
		api.setWarrantyDeed(deed);
		api.getWarrantyDeed1("25-94-194-2864");
		//deed = store.get("420-86-16-40");
		
		
		//store.persist(deed);
		//deed = store.get("208-941-194-2864");
//		WarrantyDeed deed2 = manager.createWarrantyDeed("WD207-949.pdf");
//		manager.addWarrantyDeedToHouseHistory(deed2);
	}
	
	
}








