package com.seniordesign.titlesearch;

//import org.bson.Document;
//import org.bson.types.ObjectId;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.bson.types.Binary;
import com.mongodb.client.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

public class MongoDBDatabaseManager implements DatabaseManagerStore{
	private MongoDatabase mg = null;
	private static final String databaseName = "deeds";
	private MongoCollection<Document> collection;
	
	
	public MongoDBDatabaseManager() {
		MongoClient client = createClient();
		if(client != null) {
			mg = client.getDatabase(databaseName);
			if(mg == null) {
				collection = mg.getCollection("deeds");
			}
		}
	}
	
	public MongoDatabase getDB() {
		
		return mg;
	}
	
	private static MongoClient createClient() {
		MongoClient cl = null;
	
			
        MongoClientURI connectionString = new MongoClientURI("mongodb://admin:WVEFPQXQDDBCIDGH@portal-ssl165-56.bmix-dal-yp-3c34fcfa-a90a-4f45-9384-25deebc35ef1.3180207644.composedb.com:21871,portal-ssl165-54.bmix-dal-yp-3c34fcfa-a90a-4f45-9384-25deebc35ef1.3180207644.composedb.com:21871/compose?authSource=admin&ssl=true");
        MongoClient mongoClient = new MongoClient(connectionString);
        cl = mongoClient;
		
		return cl;
		
		
	}
	
	public Collection<WarrantyDeed> getAll() {
		
		List<WarrantyDeed> wds = new ArrayList<WarrantyDeed>();
		collection = mg.getCollection("deeds");
		
		List<WarrantyDeed> w = new ArrayList<WarrantyDeed>();
		List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());
		int count = 0;
		for(Document d : documents) {
			WarrantyDeed wd = new WarrantyDeed();
			List<Object> grantees = (List<Object>) d.get("grantees");
			Object[] o = grantees.toArray();
			
			String[] strArray = Arrays.copyOf(o, o.length, String[].class);
			
			List<Object> grantors = (List<Object>) d.get("grantors");
			Object[] a = grantors.toArray();
			
			String[] strArray1 = Arrays.copyOf(a, a.length, String[].class);
			
			Binary byteArrayInBinary = (Binary) d.get("PDF");
			byte[] byteArray = null;
			if(byteArrayInBinary != null) {				
				byteArray = byteArrayInBinary.getData();
			}
			
			wd.setBookNumber(d.getString("bookNumber"));
			wd.setPageNumber(d.getString("pageNumber"));
			wd.setParentBookNumber(d.getString("parentBookNumber"));
			wd.setParentPageNumber(d.getString("parentPageNumber"));
			wd.setText(d.getString("text"));
			wd.setTransactionDate(d.getString("transactionDate"));
			wd.setYearBought(d.getString("yearBought"));
			wd.setYearSold(d.getString("yearSold"));
			wd.setIsLatest(d.getBoolean("isLatest"));
			wd.setGrantees(strArray);
			wd.setGrantors(strArray1);
			wd.setPDF(byteArray);
		
			WarrantyDeed deed = new WarrantyDeed();
			deed = wd;
			w.add(count, deed);
			count = count + 1;
		}
		
	
		return w;
	}
	
	/**
	 * Don't really need this function but I included it in case
	 */
	public WarrantyDeed get(String id) {
		collection = mg.getCollection("deeds");
		
		
		
	
		Document doc = new Document();
		
		
		FindIterable<Document> documents = collection.find(Filters.eq("id", id));
		ArrayList<Document> docs = new ArrayList();
		documents.into(docs);
		
		for(Document doc1 : docs) {
			if(doc1.get("id").equals(id)) {
				doc = doc1;
				break;
			}
		}
	
		List<Object> grantees = (List<Object>) doc.get("grantees");
		Object[] o = grantees.toArray();
		
		String[] strArray = Arrays.copyOf(o, o.length, String[].class); 
		
		List<Object> grantors = (List<Object>) doc.get("grantors");
		Object[] a = grantors.toArray();
		
		String[] strArray1 = Arrays.copyOf(a, a.length, String[].class);
		
		String byteArrayInString = (String) doc.get("pdf");
		byte[] byteArray = byteArrayInString.getBytes();
		
		WarrantyDeed newWarrantyDeed = new WarrantyDeed();
		newWarrantyDeed.setBookNumber(doc.getString("bookNumber"));
		newWarrantyDeed.setPageNumber(doc.getString("pageNumber"));
		newWarrantyDeed.setParentBookNumber(doc.getString("parentBookNumber"));
		newWarrantyDeed.setParentPageNumber(doc.getString("parentPageNumber"));
		newWarrantyDeed.setText(doc.getString("text"));
		newWarrantyDeed.setTransactionDate(doc.getString("transactionDate"));
		newWarrantyDeed.setYearBought(doc.getString("yearBought"));
		newWarrantyDeed.setYearSold(doc.getString("yearSold"));
		newWarrantyDeed.setIsLatest(doc.getBoolean("isLatest"));
		newWarrantyDeed.setGrantees(strArray);
		newWarrantyDeed.setGrantors(strArray1);
		newWarrantyDeed.setPDF(byteArray);
		
		
		return newWarrantyDeed;
	}
	
	/*
	 * stores the object into the database
	 */
	
	public WarrantyDeed store(WarrantyDeed wd) {
		collection = mg.getCollection("deeds");
		
		Document doc = new Document("WarrantyDeed", wd.getID());
		List<String> grantees = Arrays.asList(wd.getGrantees());
		List<String> grantors = Arrays.asList(wd.getGrantors());
		
		Document update = new Document("id",wd.getID())
				.append("bookNumber", wd.getBookNumber())
				.append("pageNumber", wd.getPageNumber())
				.append("parentPageNumber", wd.getParentPageNumber())
				.append("parentBookNumber", wd.getParentBookNumber())
				.append("text", wd.getText())
				.append("yearBought", wd.getYearBought())
				.append("yearSold", wd.getYearSold())
				.append("isLatest", wd.getIsLatest())
				.append("transactionDate", wd.getTransactionDate())
				.append("grantees", grantees)
				.append("grantors", grantors)
				.append("PDF", wd.getPDF());
		InsertOneOptions insertOneOptions = new InsertOneOptions();
		collection.insertOne(update, insertOneOptions);
		
		FindIterable<Document> documents = collection.find();
		
		
		
	
		
		return wd;
	}
	
	public void delete(String id) {
		collection = mg.getCollection("deeds");
		DeleteResult deleteResult = collection.deleteOne(Filters.eq("id", id));
	}
	
	
	
}