package com.seniordesign.titlesearch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.Collection;
import com.ibm.watson.developer_cloud.discovery.v1.model.DeleteDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentStatus;
import com.ibm.watson.developer_cloud.discovery.v1.model.Environment;
import com.ibm.watson.developer_cloud.discovery.v1.model.GetDocumentStatusOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListCollectionsOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListCollectionsResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListEnvironmentsOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListEnvironmentsResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.Notice;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

public class DiscoveryAPI {
	private String WARRANTY_DEEDS_COLLECTION;
	private String VERSION;
	private String URL;
	private String API_KEY;
	private static Discovery discovery = null;
	private String environmentId = "";
	private String collectionId = "";
	
	public DiscoveryAPI() {
		ConfigProperties properties = new ConfigProperties();

		WARRANTY_DEEDS_COLLECTION = properties.getProperty("DISCOVERY_COLLECTION");
		VERSION = properties.getProperty("DISCOVERY_VERSION");
		URL = properties.getProperty("DISCOVERY_ENDPOINT");
		API_KEY = properties.getProperty("DISCOVERY_APIKEY");
		discovery = connectToDiscovery();
		environmentId = getEnvironmentId();
		collectionId = getCollectionId();
	}

	private Discovery connectToDiscovery() {
		Discovery discovery = new Discovery(VERSION);
		discovery.setEndPoint(URL);

		IamOptions options = new IamOptions.Builder()
		  .apiKey(API_KEY)
		  .build();
		discovery.setIamCredentials(options);
		
		return discovery;
	}
	
	private String getEnvironmentId() {
		String environmentId = "";
		ListEnvironmentsOptions listOptions = new ListEnvironmentsOptions.Builder().build();
		ListEnvironmentsResponse listResponse = discovery.listEnvironments(listOptions).execute();
		
		for(Environment environment : listResponse.getEnvironments()) {
			if (!environment.isReadOnly()) {
		        environmentId = environment.getEnvironmentId();
		        break;
		      }
		}
		return environmentId;
	}
	
	private String getCollectionId() {
		String collectionId = "";
		String environmentId = this.getEnvironmentId();
		
		ListCollectionsOptions listOptions = new ListCollectionsOptions.Builder(environmentId).build();
		ListCollectionsResponse listResponse = discovery.listCollections(listOptions).execute();
		for(Collection collection : listResponse.getCollections()) {
			if(collection.getName().equals(WARRANTY_DEEDS_COLLECTION)) {
				collectionId = collection.getCollectionId();
				break;
			}
		}
		return collectionId;
	}
	
	public DocumentAccepted addDocumentToCollection(AddDocumentOptions.Builder builder) {
		builder.collectionId(collectionId);
		builder.environmentId(environmentId);
		AddDocumentOptions options = builder.build();
		DocumentAccepted document = null;
		if(options.file() != null) {
			document = discovery.addDocument(options).execute();
		}
		return document;
	}
	
	public DocumentStatus getDocumentStatus(GetDocumentStatusOptions.Builder builder) {
		builder.collectionId(collectionId);
		builder.environmentId(environmentId);
		GetDocumentStatusOptions options = builder.build();
		DocumentStatus status = discovery.getDocumentStatus(options).execute();
		return status;
	}
	
	public boolean deleteDocument(String documentId) {
		DeleteDocumentOptions.Builder builder = new DeleteDocumentOptions.Builder();
		builder.collectionId(collectionId);
		builder.environmentId(environmentId);
		builder.documentId(documentId);
		DeleteDocumentOptions options = builder.build();
		try {
			discovery.deleteDocument(options).execute();
			return true;
		} catch (Error err) {
			System.out.println(err);
		}
		return false;
	}
	
	public QueryResponse runQuery(QueryOptions.Builder builder) {
		builder.collectionId(collectionId);
		builder.environmentId(environmentId);
		QueryOptions options = builder.build();
		QueryResponse response = discovery.query(options).execute();
		return response;
	}
	
	public void populateWarrantyDeedText(WarrantyDeed wd) {
		if(wd == null) {
			return;
		}
		InputStream fileContent = new ByteArrayInputStream(wd.getPDF());
			
		if(wd.getBookNumber().isEmpty() || wd.getPageNumber().isEmpty()) {
			
		}
		
		String fileName = "WD" + wd.getBookNumber() + "-" + wd.getPageNumber() + ".pdf";
		DocumentAccepted document = null;
		
		if(!checkIfFileExistsInDiscovery(fileName)) {					
			AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder();
			builder.file(fileContent);
			builder.fileContentType("application/pdf");
			builder.filename(fileName);
			document = addDocumentToCollection(builder);
			waitForDiscoveryToProcessDocument(fileName, document);
		}
		String text = getOCRTextFromDiscovery(fileName);
		wd.setText(text);
	}
	
	private void waitForDiscoveryToProcessDocument(String fileName, DocumentAccepted document) {
		if(fileName.isEmpty() || document == null) {
			return;
		}
		
		final GetDocumentStatusOptions.Builder statusBuilder = new GetDocumentStatusOptions.Builder();
		
		try {
			statusBuilder.documentId(document.getDocumentId());
		} catch(NullPointerException e) {
			e.printStackTrace();
			return;
		}
		
		boolean isDocumentProcessed = false;
		
		while(!isDocumentProcessed) {
			DocumentStatus status = getDocumentStatus(statusBuilder);
			if(status.getStatus().equals("available") || status.getStatus().equals("available with notices")) {
				isDocumentProcessed = true;
			} else if(status.getStatus().equals("failed")) {		
				isDocumentProcessed = true;
				System.out.println("Discovery failed to process file.");
				List<Notice> notices = status.getNotices();
				for(Notice notice : notices) {
					System.out.println("Document ID: " + notice.getDocumentId());
					System.out.println("Notice ID: " + notice.getNoticeId());
					System.out.println("Severity: " + notice.getSeverity());
					System.out.println("Step error occured: " + notice.getStep());
					System.out.println("Description of error: " + notice.getDescription());
				}
			}
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String getOCRTextFromDiscovery(String fileName) {
		QueryOptions.Builder builder = new QueryOptions.Builder();
		String filterQuery = "extracted_metadata.filename::";
		filterQuery += fileName;
		builder.filter(filterQuery);
		QueryResponse queryResponse = runQuery(builder);
		
		
		List<QueryResult> results = queryResponse.getResults();
		HashMap<String, Object> metadata = results.get(0);
		String newText = "";
		
		if(metadata.containsKey("text")) {
			String text = metadata.get("text").toString();
			byte[] textInBytes = text.getBytes();
			
			try {
				newText = new String(textInBytes, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			newText += " ";
			
			if(metadata.containsKey("landdescription")) {
				String landDescription = metadata.get("landdescription").toString();
				byte[] landDescriptionInBytes = landDescription.getBytes();
				String newLandDescription = "";
				try {
					newLandDescription = new String(landDescriptionInBytes, "US-ASCII");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newText += newLandDescription;
			}
		}
		
		return newText;
	}
	
	// Avoid uploading the same files to Discovery more than once.
	private boolean checkIfFileExistsInDiscovery(String filename) {
		QueryOptions.Builder builder = new QueryOptions.Builder();
		String filterQuery = "extracted_metadata.filename::";
		filterQuery += filename;
		builder.filter(filterQuery);
		
		QueryResponse queryResponse = runQuery(builder);
		List<QueryResult> results = queryResponse.getResults();
		if(results.isEmpty()) {
			return false;
		}
		return true;
	}
	
//	public static void main(String[] args) {
//		DiscoveryAPI api = new DiscoveryAPI();
//		TitleSearchManager manager = TitleSearchManager.getInstance();
//		WarrantyDeed deed = manager.createWarrantyDeed("WD207-832.pdf");
//		deed.setBookNumber("207");
//		deed.setPageNumber("832");
//		api.populateWarrantyDeedText(deed);
//		System.out.println("WD TEXT: " + deed.getText());
//	}
}