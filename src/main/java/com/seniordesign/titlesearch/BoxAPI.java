package com.seniordesign.titlesearch;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.box.sdk.BoxAPIResponseException;
import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.DeveloperEditionEntityType;
import com.box.sdk.IAccessTokenCache;
import com.box.sdk.InMemoryLRUAccessTokenCache;

enum Folder {
	WARRANTYPDF, WARRANTYTEXT;
}

public class BoxAPI {
	private static BoxDeveloperEditionAPIConnection client = null;
	private String folderId = "64984958567";
	private final String userId = "7110130040";
	
	public BoxAPI(Folder folder) {
		switch(folder) {
			case WARRANTYPDF:
				folderId = "64984671696";
				break;
			case WARRANTYTEXT:
				folderId = "64984958567";
				break;
			default:
				break;
		}
		Reader reader = null;
		BoxConfig boxConfig = null;
		try {
			reader = new FileReader("apps/myapp.war/WEB-INF/config.json");
			boxConfig = BoxConfig.readFrom(reader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int MAX_CACHE_ENTRIES = 100;
		IAccessTokenCache accessTokenCache = new 
		  InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);

		// Create new app enterprise connection object
		client = new BoxDeveloperEditionAPIConnection(userId, DeveloperEditionEntityType.USER, boxConfig, accessTokenCache);
	}
	
	public void uploadFile(InputStream stream, String fileName) throws IOException {
		// Select Box folder
		BoxFolder folder = new BoxFolder(client, folderId);
		
		System.out.println(stream.toString());
		// Upload file
		try {
			BoxFile.Info newFileInfo = folder.uploadFile(stream, fileName);
			System.out.println(newFileInfo.getID());
		} catch (BoxAPIResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getResponse());
		}
	}
}
