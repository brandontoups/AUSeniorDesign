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
	@GET
	@Path("/")
	@Produces("{application/json")
	public WarrantyDeed getWarrantyDeed(String bookNumber, String pageNumber) {
		WarrantyDeed wd = new WarrantyDeed();
		//List<String> bookNum = new ArrayList<String>();
		for (WarrantyDeed deed : store.getAll()) {
			//We know that we have bookNumber in the parameter, so we first need to get 
			//the bookNumer from the list and then compare it
			String bookNumber1 = deed.getBookNumber();
			if(bookNumber == bookNumber1) {
				//List<String> pageNum = new ArrayList<String>();
				String pageNumber1 = deed.getPageNumber();
				if(pageNumber == pageNumber1) {
					wd = deed;
				}
			}
		}
		return wd;
	}
	
	
	public WarrantyDeed getWarrantyDeed1(String id) {
		WarrantyDeed wd = new WarrantyDeed();
		for (WarrantyDeed deed : store.getAll()) {
			String ids = deed.getID();
			if(id.equals(ids)) {
				wd = deed;
			}
		}
		return wd;
	}
	
	public WarrantyDeed getWarrantyDeed2(String name) {
		WarrantyDeed wd = new WarrantyDeed();
		for (WarrantyDeed deed : store.getAll()) {
			String[] names = deed.getGrantors();
			for(int i = 0; i < names.length; i++) {
				if(name.equals(names[i])) {
					wd = deed;
				}
			}
		}
		return wd;
	}
	/*
	 * takes in a warrantyDeed object and passes it on to the database
	 */
	public WarrantyDeed setWarrantyDeed(WarrantyDeed wd) {
		store.persist(wd);
		return wd;
	}
	
	
}








