package com.seniordesign.titlesearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PerformTitleSearch
 */
@WebServlet(name = "/PerformTitleSearch", urlPatterns={"/search", "/warranty/validate", "/warranty/history"})
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
        //TitleSearchManager.main(new String[0]);
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	//TitleSearchManager.main(new String[0]);    	
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
			String bookNo = request.getParameter("book");
			String pageNo = request.getParameter("page");
			try {
				Integer.parseInt(bookNo);
				Integer.parseInt(pageNo);
			} catch (NumberFormatException e) {
				response.sendRedirect("/");
			}
			if(manager.getHouseHistory().size() > 0) {
				manager.resetHouseHistory();
			}
			nextWD = manager.getWarrantyDeed(bookNo, pageNo);
			manager.addWarrantyDeedToHouseHistory(nextWD);
			response.sendRedirect("/warranty/history");
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
			case "updateWD":
				updateWarrantyDeedText(request, response);
				response.sendRedirect("/warranty/history");
				break;
			case "getNextWD":
				if(manager.getHouseHistory().size() != 0) {					
					getWarrantyDeed(request, response);
					response.sendRedirect("/warranty/history");
				} else {
					response.sendRedirect("/");
				}
			default:
				break;
		}
	}
	
	protected void getWarrantyDeed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bookNo = request.getParameter("suggestedBookNumber");
		String pageNo = request.getParameter("suggestedPageNumber");
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
		String deedNumber = request.getParameter("deed");
		if(history.size() > 0) {
			try {
				int deedNumberInInteger = Integer.parseInt(deedNumber);
				if(deedNumberInInteger < 0 || history.size() < deedNumberInInteger) {				
					response.sendRedirect("/");
					return;
				}
			} catch (NumberFormatException | NullPointerException e) {
				response.sendError(400, "Bad Request");
				return;
			}
			ServletContext context = this.getServletContext();
			context.setAttribute("history", history);
			request.getRequestDispatcher("/warranty/validate.jsp").forward(request, response);
		} else {
			response.sendRedirect("/");
			return;
		}
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
			return;
		}
		if(index >= 0) {
			WarrantyDeed validatedWD = new WarrantyDeed();
			validatedWD.setBookNumber(request.getParameter("bookNumber"));
			validatedWD.setPageNumber(request.getParameter("pageNumber"));
			validatedWD.setGrantors(checkForEmptyFields(request.getParameterValues("grantor")));
			validatedWD.setGrantees(checkForEmptyFields(request.getParameterValues("grantee")));
			validatedWD.setParentBookNumber(request.getParameter("parentBookNumber"));
			validatedWD.setParentPageNumber(request.getParameter("parentBookPageNumber"));
			validatedWD.setText(request.getParameter("legalDescription"));
			validatedWD.setTransactionDate(request.getParameter("transaction"));
			validatedWD.setYearSold(request.getParameter("yearSold"));
			validatedWD.setYearBought(request.getParameter("yearBought"));
			String isLatest = request.getParameter("isLatest");
			if(isLatest != null) {				
				validatedWD.setIsLatest(true);
			} else {
				validatedWD.setIsLatest(false);
			}
			manager.updateWarrantyDeed(index, validatedWD);
			
//			WarrantyDeed deedToUpdate = manager.getHouseHistory().get(index);
//			String[] grantors = checkForEmptyFields(request.getParameterValues("grantor"));
//			String[] grantees = checkForEmptyFields(request.getParameterValues("grantee"));
//			String transaction = request.getParameter("transaction");
//			String bookNumber = request.getParameter("bookNumber");
//			String pageNumber = request.getParameter("pageNumber");
//			String yearSold = request.getParameter("yearSold");
//			String yearBought = request.getParameter("yearBought");
//			String parentBookNumber = request.getParameter("parentBookNumber");
//			String parentBookPageNumber = request.getParameter("parentBookPageNumber");
//			String legalDescription = request.getParameter("legalDescription");
//			String isLatest = request.getParameter("isLatest");
//			deedToUpdate.setGrantors(grantors);
//			deedToUpdate.setGrantees(grantees);
//			deedToUpdate.setTransactionDate(transaction);
//			deedToUpdate.setBookNumber(bookNumber);
//			deedToUpdate.setPageNumber(pageNumber);
//			deedToUpdate.setYearBought(yearBought);
//			if(!yearSold.equals("")) {				
//				deedToUpdate.setYearSold(yearSold);
//			}
//			deedToUpdate.setParentBookNumber(parentBookNumber);
//			deedToUpdate.setParentPageNumber(parentBookPageNumber);
//			deedToUpdate.setText(legalDescription);
//			if(isLatest != null) {				
//				deedToUpdate.setIsLatest(true);
//			} else {
//				deedToUpdate.setIsLatest(false);
//			}
		}
	}
	
	// Check if any of the grantor and grantee input fields are empty. Since the user can add as many grantors and grantees as he wants,
	// we need to be able to check if the user hasn't submitted a large number of empty fields. Also, since the user can add more grantors
	// and grantees, we have to create a new String array to put into the WarrantyDeed object.
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
