package com.seniordesign.titlesearch;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class PerformTitleSearch
 */
@WebServlet(name = "/PerformTitleSearch", urlPatterns={"/", "/search", "/warranty/validate", "/warranty/history"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,
maxFileSize = 1024 * 1024 * 10, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class PerformTitleSearch extends HttpServlet {
	
	TitleSearchManager manager = TitleSearchManager.getInstance();
	WarrantyDeed nextWD = new WarrantyDeed();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PerformTitleSearch() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	TitleSearchManager.main(new String[0]);    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uri = request.getRequestURI();
		if(uri.endsWith("/")) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} else if(uri.endsWith("/warranty/validate")) {
			validateReport(request, response);
		} else if(uri.endsWith("/search")) {
			getWarrantyDeed(request, response);
		} else if(uri.endsWith("/warranty/history")) {
			buildReport(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		switch(action) {
			case "showWD":
				break;
			case "updateText":
				updateWarrantyDeedText(request, response);
				response.sendRedirect("/warranty/history");
				break;
			case "getNextWD":
				getWarrantyDeed(request, response);
				response.sendRedirect("/warranty/history");
			default:
				break;
		}
	}
	
	protected void getWarrantyDeed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bookNo = request.getParameter("book");
		String pageNo = request.getParameter("page");
		if(request.getParameter("next") != null) {
			System.out.println("Book: " + bookNo + " Page: " + pageNo);
			WarrantyDeed nextDeedInHistory = manager.getWarrantyDeed(bookNo, pageNo);
			manager.addWarrantyDeedToHouseHistory(nextDeedInHistory);
		} else if(request.getParameter("complete") != null) {
			System.out.println("History complete");
		}
	}

	protected void validateReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		File file = new File(request.getServletContext().getRealPath("") + File.separator + "warrantyDeedPDFs" + File.separator + "WD207-941.pdf");
		List<WarrantyDeed> history = manager.getHouseHistory();
		ServletContext context = this.getServletContext();
		context.setAttribute("history", history);
		request.getRequestDispatcher("/warranty/validate.jsp").forward(request, response);
	}
	
	protected void buildReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<WarrantyDeed> history = manager.getHouseHistory();
		ServletContext context = this.getServletContext();
		context.setAttribute("history", history);
		request.getRequestDispatcher("/warranty/history.jsp").forward(request, response);
	}
	
	protected void updateWarrantyDeedText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int index = -1;
		try {
			index = Integer.parseInt(request.getParameter("index"));
		} catch(NumberFormatException e) {
			
		}
		if(index != -1) {		
			WarrantyDeed deedToUpdate = manager.getHouseHistory().get(index);
			String[] grantors = checkForEmptyFields(request.getParameterValues("grantor"));
			String[] grantees = checkForEmptyFields(request.getParameterValues("grantee"));
			String transaction = request.getParameter("transaction");
			String dateFrom = request.getParameter("dateFrom");
			String dateTo = request.getParameter("dateTo");
			String parentBookNumber = request.getParameter("parentBookNumber");
			String parentBookPageNumber = request.getParameter("parentBookPageNumber");
			String legalDescription = request.getParameter("legalDescription");
			deedToUpdate.setGrantors(grantors);
			deedToUpdate.setGrantees(grantees);
			deedToUpdate.setTransactionDate(transaction);
			deedToUpdate.setYearBought(dateFrom);
			deedToUpdate.setYearSold(dateTo);
			deedToUpdate.setParentBookNumber(parentBookNumber);
			deedToUpdate.setParentPageNumber(parentBookPageNumber);
			deedToUpdate.setText(legalDescription);
		}
		
	}
	
	private String[] checkForEmptyFields(String[] list) {
		List<String> newList = new ArrayList<String>();
		for(int i = 0; i < list.length; i++) {
			if(!list[i].isEmpty()) {
				newList.add(list[i]);
			}
		}
		String[] result = new String[newList.size()];
		newList.toArray(result);
		return result;
	}
}
