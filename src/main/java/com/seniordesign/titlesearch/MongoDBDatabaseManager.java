package com.seniordesign.titlesearch;

//import org.bson.Document;
//import org.bson.types.ObjectId;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.types.Binary;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class MongoDBDatabaseManager implements DatabaseManagerStore {
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
		String url = "mongodb://admin:NOOEWWGDKPTOQDRP@sl-us-south-1-portal.49.dblayer.com:19885,sl-us-south-1-portal.53.dblayer.com:19885/compose?authSource=admin&ssl=true";
		MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
		return mongoClient;
		
	}
	
	public Collection<WarrantyDeed> getAll() {
		
		List<WarrantyDeed> wds = new ArrayList<WarrantyDeed>();
		try {
			MongoIterable<String> listDatabaseNames = createClient().listDatabaseNames();
			Document document;
			WarrantyDeed warrantyDeed = new WarrantyDeed();
			MongoCursor<Document> cursor = collection.find().iterator();
			try {
				while(cursor.hasNext()) {
					document = cursor.next();
					ArrayList<Document> grantees = (ArrayList<Document>) document.get("grantees");
					String[] grantees1 = (String[]) grantees.toArray();
					
					ArrayList<Document> grantors = (ArrayList<Document>) document.get("grantors");
					String[] grantor = (String[]) grantees.toArray();
					
					byte[] outputBytes = ((Binary)document.get("PDF")).getData();
					
					warrantyDeed.setBookNumber(document.getString("bookNumber"));
					warrantyDeed.setPageNumber(document.getString("pageNumber"));
					warrantyDeed.setParentBookNumber(document.getString("parentBookNumber"));
					warrantyDeed.setParentPageNumber(document.getString("parentPageNumber"));
					warrantyDeed.setText(document.getString("text"));
					warrantyDeed.setTransactionDate(document.getString("transactionDate"));
					warrantyDeed.setYearBought(document.getString("yearBought"));
					warrantyDeed.setYearSold(document.getString("yearSold"));
					warrantyDeed.setIsLatest(document.getBoolean("isLatest"));
					warrantyDeed.setGrantees(grantees1);
					warrantyDeed.setGrantors(grantor);
					warrantyDeed.setPDF(outputBytes);
					warrantyDeed.updateID();
					wds.add(warrantyDeed);
				}
			}finally {
				cursor.close();
			}
		} catch (Exception e) {
			return null;
		}
		return wds;
	}
	
	public WarrantyDeed get(String id) {
		collection = mg.getCollection("deeds");
		Document doc = collection.find(Filters.eq("id", id)).first();
		ArrayList<Document> grantees = (ArrayList<Document>) doc.get("grantees");
		String[] grantees1 = (String[]) grantees.toArray();
		
		ArrayList<Document> grantors = (ArrayList<Document>) doc.get("grantors");
		String[] grantor = (String[]) grantees.toArray();
		
		byte[] outputBytes = ((Binary)doc.get("PDF")).getData();
		
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
		newWarrantyDeed.setGrantees(grantees1);
		newWarrantyDeed.setGrantors(grantor);
		newWarrantyDeed.setPDF(outputBytes);
		newWarrantyDeed.updateID();
		return newWarrantyDeed;
	}
	
	/*
	 * persist function
	 */
	
	public WarrantyDeed persist(WarrantyDeed wd) {
		collection = mg.getCollection("deeds");
		Document doc = new Document("WarrantyDeed", wd.getID());
		Document update = new Document("$set", new Document()
				.append("bookNumber", wd.getBookNumber())
				.append("pageNumber", wd.getBookNumber())
				.append("parentPageNumber", wd.getParentPageNumber())
				.append("parentBookNumber", wd.getParentBookNumber())
				.append("text", wd.getText())
				.append("yearBought", wd.getYearBought())
				.append("yearSold", wd.getYearSold())
				.append("isLatest", wd.getIsLatest())
				.append("transactionDate", wd.getTransactionDate())
				.append("grantees", wd.getGrantees())
				.append("grantors", wd.getGrantors())
				.append("PDF", wd.getPDF())
				.append("id", wd.getID()));
		UpdateOptions updateOptions = new UpdateOptions().upsert(true);
		collection.updateOne(doc, update, updateOptions);
		wd.updateID();
		return wd;
	}
	
	public void delete(String id) {
		collection = mg.getCollection("deeds");
		DeleteResult deleteResult = collection.deleteOne(Filters.eq("id", id));
	}
	
	
	
}