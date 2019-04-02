package com.seniordesign.titlesearch;

public class DatabaseManagerStoreInstance{
	private static DatabaseManagerStore instance;
	static {
		MongoDBDatabaseManager cvif = new MongoDBDatabaseManager();
		if(cvif.getDB() != null) {
			instance = cvif;
		}
	} 
	public static DatabaseManagerStore getInstance() {
		return instance;
		
	}
}