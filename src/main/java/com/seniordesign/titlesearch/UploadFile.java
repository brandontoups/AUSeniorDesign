package com.seniordesign.titlesearch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentStatus;
import com.ibm.watson.developer_cloud.discovery.v1.model.GetDocumentStatusOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.ibm.watson.developer_cloud.util.ResponseUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.seniordesign.titlesearch.BoxAPI;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,
maxFileSize = 1024 * 1024 * 10, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		final String fileName;
		Part filePart = request.getPart("fileInput");
		String path = filePart.getSubmittedFileName();
		fileName = Paths.get(path).getFileName().toString();
		InputStream fileContent = filePart.getInputStream();
		
		final DiscoveryAPI collection = new DiscoveryAPI();
		AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder();
		builder.file(fileContent);
		builder.fileContentType(filePart.getContentType());
		builder.filename(fileName);
		System.out.println(fileName);
		DocumentAccepted document = collection.addDocumentToCollection(builder);
		final GetDocumentStatusOptions.Builder statusBuilder = new GetDocumentStatusOptions.Builder();
		statusBuilder.documentId(document.getDocumentId());
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				DocumentStatus status = collection.getDocumentStatus(statusBuilder);
				if(status.getStatus().equals("available") || status.getStatus().equals("available with notices")) {
					createTextFile(fileName);
					System.out.println("Success, document has been added.");
					timer.cancel();
					timer.purge();
				}
				System.out.println(status.getStatus());
			}
		}, 1000, 20000);
		request.setAttribute("document", document);
		request.getRequestDispatcher("ocr.jsp").forward(request, response);
	}
	
	private void createTextFile(String fileName) {
		// TODO Auto-generated method stub
		System.out.println("Entered the function");
		QueryOptions.Builder builder = new QueryOptions.Builder();
		String filterQuery = "extracted_metadata.filename::";
		filterQuery += fileName;
		builder.filter(filterQuery);
		DiscoveryAPI api = new DiscoveryAPI();
		QueryResponse queryResponse = api.runQuery(builder);
		List<QueryResult> results = queryResponse.getResults();
		Long resultCount = queryResponse.getMatchingResults();
		System.out.println(resultCount);
		for(int i = 0; i < resultCount; i++) {
			System.out.println("Inside for loop");
			HashMap<String, Object> metadata = results.get(i);
			JsonObject extractedMetadata = ResponseUtils.getJsonObject(metadata.get("extracted_metadata").toString());
			if(extractedMetadata.get("filename").getAsString().equals(fileName)) {
				String fileContent = "";
				if(metadata.containsKey("text")) {
					fileContent += metadata.get("text");
				}
				if(metadata.containsKey("landdescription")) {
					fileContent += metadata.get("landdescription").toString();
				}
				
				StringTokenizer splitFileName = new StringTokenizer(fileName, ".");
				String textFileName = splitFileName.nextToken() + ".txt";
				
				try {
					ByteArrayInputStream stream = new ByteArrayInputStream(fileContent.getBytes());
					BoxAPI boxAPI = new BoxAPI();
					boxAPI.uploadFile(stream, textFileName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
