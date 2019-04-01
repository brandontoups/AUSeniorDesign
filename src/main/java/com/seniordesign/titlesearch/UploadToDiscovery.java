package com.seniordesign.titlesearch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ibm.watson.developer_cloud.discovery.v1.model.AddDocumentOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.DocumentAccepted;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult;

/**
 * Servlet implementation class UploadToDiscovery
 */
@WebServlet("/UploadToDiscovery")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,
maxFileSize = 1024 * 1024 * 10, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadToDiscovery extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadToDiscovery() {
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
				String fileName;
				Part filePart = request.getPart("fileInput");
				String path = filePart.getSubmittedFileName();
				fileName = Paths.get(path).getFileName().toString();
				InputStream fileContent = filePart.getInputStream();
				
				if(!checkIfFileExistsInDiscovery(fileName)) {					
					DiscoveryAPI collection = new DiscoveryAPI();
					AddDocumentOptions.Builder builder = new AddDocumentOptions.Builder();
					builder.file(fileContent);
					builder.fileContentType(filePart.getContentType());
					builder.filename(fileName);
					System.out.println(fileName);
					DocumentAccepted document = collection.addDocumentToCollection(builder);
					request.setAttribute("document", document);
					request.setAttribute("statusDesc", "Added file to discovery.");
					request.setAttribute("statusCode", "200");
					request.getRequestDispatcher("upload.jsp").forward(request, response);
				}
				request.setAttribute("statusDesc", "File already exists for discovery.");
				request.setAttribute("statusCode", "400");
				request.getRequestDispatcher("upload.jsp").forward(request, response);
	}
	
	private boolean checkIfFileExistsInDiscovery(String filename) {
		QueryOptions.Builder builder = new QueryOptions.Builder();
		String filterQuery = "extracted_metadata.filename::";
		filterQuery += filename;
		builder.filter(filterQuery);
		DiscoveryAPI api = new DiscoveryAPI();
		QueryResponse queryResponse = api.runQuery(builder);
		List<QueryResult> results = queryResponse.getResults();
		if(results.isEmpty()) {
			return false;
		}
		return true;
	}

}
