package com.seniordesign.titlesearch;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.ibm.watson.developer_cloud.discovery.v1.model.Notice;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.ibm.watson.developer_cloud.util.ResponseUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.seniordesign.titlesearch.BoxAPI;

/**
 * Servlet implementation class UploadFile
 */
@WebServlet("/UploadFile")
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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		final String fileName = (String) request.getAttribute("fileName");
		BufferedInputStream fileContent = (BufferedInputStream) request.getAttribute("fileContent");
		String fileContentType = (String) request.getAttribute("fileContentType");
		
		// Send the file to Discovery to be scanned using OCR.
		final DiscoveryAPI collection = new DiscoveryAPI();
		AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder();
		builder.file(fileContent);
		builder.fileContentType(fileContentType);
		builder.filename(fileName);
		System.out.println(fileName);
		DocumentAccepted document = collection.addDocumentToCollection(builder);
		if(fileContent != null) {
			fileContent.close();
		}
		
		/*
		 * I'm using a Timer to check the status of the Document every 20 seconds while we wait for Discovery
		 * to scan the document using OCR. Also the reason why some variables have the final keyword attached to
		 * their declaration.
		 */
		final GetDocumentStatusOptions.Builder statusBuilder = new GetDocumentStatusOptions.Builder();
		try {
			statusBuilder.documentId(document.getDocumentId());
		} catch(NullPointerException e) {
			e.printStackTrace();
			return;
		}
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				DocumentStatus status = collection.getDocumentStatus(statusBuilder);
				if(status.getStatus().equals("available") || status.getStatus().equals("available with notices")) {
					// Discovery is done processing. We can now grab the OCR'd text.
					createTextFile(fileName);
					timer.cancel();
					timer.purge();
				} else if(status.getStatus().equals("failed")) {
					System.out.println("Discovery failed to process file.");
					List<Notice> notices = status.getNotices();
					for(Notice notice : notices) {
						System.out.println("Document ID: " + notice.getDocumentId());
						System.out.println("Notice ID: " + notice.getNoticeId());
						System.out.println("Severity: " + notice.getSeverity());
						System.out.println("Step error occured: " + notice.getStep());
						System.out.println("Description of error: " + notice.getDescription());
					}
					timer.cancel();
					timer.purge();
				}
			}
		}, 1000, 20000);
	}
	
	private void createTextFile(String fileName) {
		// TODO Auto-generated method stub
		System.out.println("Creating the .txt file");
		
		/*
		 * We are filtering the query by the fileName to have the max guarantee that we will receive 1 match.
		 */
		QueryOptions.Builder builder = new QueryOptions.Builder();
		String filterQuery = "extracted_metadata.filename::";
		filterQuery += fileName;
		builder.filter(filterQuery);
		DiscoveryAPI api = new DiscoveryAPI();
		QueryResponse queryResponse = api.runQuery(builder);
		List<QueryResult> results = queryResponse.getResults();
		Long resultCount = queryResponse.getMatchingResults();

		
		for(int i = 0; i < resultCount; i++) {
			HashMap<String, Object> metadata = results.get(i);
			// Using classes provided by the IBM Developer Cloud Java SDK to compatibility.
			JsonObject extractedMetadata = ResponseUtils.getJsonObject(metadata.get("extracted_metadata").toString());
			if(extractedMetadata.get("filename").getAsString().equals(fileName)) {
				String fileContent = "";
				if(metadata.containsKey("text")) {
					fileContent += metadata.get("text");
				}
				fileContent += " "; // Adding a space between text and land description.
				if(metadata.containsKey("landdescription")) {
					fileContent += metadata.get("landdescription").toString();
				}
				
				/*
				 * Using a tokenizer because I need to remove the .pdf extension and change it to .txt
				 */
				StringTokenizer splitFileName = new StringTokenizer(fileName, ".");
				String textFileName = splitFileName.nextToken() + ".txt";
				
				try {
					ByteArrayInputStream stream = new ByteArrayInputStream(fileContent.getBytes("US-ASCII"));
					BoxAPI boxAPI = new BoxAPI(Folder.WARRANTYTEXT);
					boxAPI.uploadFile(stream, textFileName);
					stream.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
