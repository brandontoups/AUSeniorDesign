package com.seniordesign.titlesearch;
/*
 * DatabaseMangerStore() file
 */


import java.util.Collection;

/*
 * Define the API for a store
 */

public interface DatabaseManagerStore {
	public Collection<WarrantyDeed> getAll();
	
	
	public Object getDB();
	

	public WarrantyDeed get(String id);
	/*
	 * Put a WarrantyDeed to the store
	 */
	public WarrantyDeed persist(WarrantyDeed wd);
	
	public void delete(String id);
}
