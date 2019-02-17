package com.seniordesign.titlesearch;

import java.util.List;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.Collection;
import com.ibm.watson.developer_cloud.discovery.v1.model.DeleteDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentStatus;
import com.ibm.watson.developer_cloud.discovery.v1.model.Environment;
import com.ibm.watson.developer_cloud.discovery.v1.model.Field;
import com.ibm.watson.developer_cloud.discovery.v1.model.GetDocumentStatusOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListCollectionFieldsResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListCollectionsOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListCollectionsResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListEnvironmentsOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListEnvironmentsResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.ListFieldsOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

public class DiscoveryAPI {
	
	private static final String WARRANTY_DEEDS_COLLECTION = System.getenv("DISCOVERY_COLLECTION");
	private static final String VERSION = System.getenv("DISCOVERY_VERSION");
	private static final String URL = System.getenv("DISCOVERY_ENDPOINT");
	private static final String API_KEY = System.getenv("DISCOVERY_APIKEY");
	private static Discovery discovery = null;
	private String environmentId = "";
	private String collectionId = "";
	
	public DiscoveryAPI() {
		super();
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
	
	public List<Field> listFields() {
		ListFieldsOptions.Builder builder = new ListFieldsOptions.Builder();
		builder.environmentId(environmentId);
		builder.addCollectionIds(collectionId);
		ListFieldsOptions listFieldsOptions = builder.build();
		ListCollectionFieldsResponse response = discovery.listFields(listFieldsOptions).execute();
		List<Field> fields = response.getFields();
		return fields;
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
}