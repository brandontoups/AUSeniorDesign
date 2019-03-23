package com.seniordesign.titlesearch;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;

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
		String query = request.getParameter("query");
		QueryOptions.Builder builder = new QueryOptions.Builder();
		String filterQuery = "extracted_metadata.filename::";
		filterQuery += query;
		builder.filter(filterQuery);
		DiscoveryAPI api = new DiscoveryAPI();
		QueryResponse queryResponse = api.runQuery(builder);
		request.setAttribute("queryResponse", queryResponse);
		request.getRequestDispatcher("query.jsp").forward(request, response);
	}

}
