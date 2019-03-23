package com.seniordesign.titlesearch;

public class DatabaseManagerStoreInstance{
	private static DatabaseManagerStore instance;
	static {
		MongoDBDatabaseManager cvif = new MongoDBDatabaseManager();
		if(cvif.getDB() != null) {
			instance = cvif;
		}
	} 
	
	/*
	 * calls the get instance function from the databaseManagerStore file
	 */
	public static DatabaseManagerStore getInstance() {
		return instance;
		
	}
}