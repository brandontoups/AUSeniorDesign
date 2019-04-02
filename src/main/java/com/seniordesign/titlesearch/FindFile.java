package com.seniordesign.titlesearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryPassages;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;
import com.ibm.watson.developer_cloud.util.ResponseUtils;

/**
 * Servlet implementation class FindFile
 */
@WebServlet("/FindFile")
public class FindFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindFile() {
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
		String action = request.getParameter("action");
		String query = request.getParameter("query");
		QueryOptions.Builder builder = new QueryOptions.Builder();
		DiscoveryAPI api = new DiscoveryAPI();
		QueryResponse queryResponse = null;
		List<QueryResult> results = null;
		List<QueryPassages> passages = null;
		HashMap<String, Object> metadata = null;
		JsonObject extractedMetadata = null;
		Long passagesCount = 1000L;
		String fileName = "";
		String newText = "";
		
		switch(action) {
			case "getPassages":
				String naturalLanguageQuery = query;
				builder.query("text:" + naturalLanguageQuery);
				builder.passages(true);
				builder.passagesCount(passagesCount);
				queryResponse = api.runQuery(builder);
				passages = queryResponse.getPassages();
				if(passages != null) {
					for(QueryPassages passage : passages) {
						try {							
							Files.write(Paths.get("passages\\" + passage.getDocumentId() + ".txt"), passage.getPassageText().getBytes("US-ASCII"), StandardOpenOption.CREATE_NEW);
							System.out.println("File added.");
						} catch (IOException e) {
						}
					}					
				} else {
					System.out.println("Passages are null for some reason");
				}
				break;
			case "findDocument":
				String filterQuery = "extracted_metadata.filename::";
				filterQuery += query;
				builder.filter(filterQuery);
				queryResponse = api.runQuery(builder);
				results = queryResponse.getResults();
				passages = queryResponse.getPassages();
				metadata = results.get(0);
				extractedMetadata = ResponseUtils.getJsonObject(metadata.get("extracted_metadata").toString());
				fileName = extractedMetadata.get("filename").getAsString();
				
				if(metadata.containsKey("text")) {
					String text = metadata.get("text").toString();
					byte[] textInBytes = text.getBytes();
					newText = new String(textInBytes, "US-ASCII");
					newText += " ";
					if(metadata.containsKey("landdescription")) {
						String landDescription = metadata.get("landdescription").toString();
						byte[] landDescriptionInBytes = landDescription.getBytes();
						String newLandDescription = new String(landDescriptionInBytes, "US-ASCII");
						newText += newLandDescription;
					}
				}
				break;
			default:
				break;
		}
		
		ServletContext context = this.getServletContext();
		context.setAttribute("fileName", fileName);
		context.setAttribute("text", newText);
		request.setAttribute("queryResponse", queryResponse);
		request.getRequestDispatcher("query.jsp").forward(request, response);
	}

}
