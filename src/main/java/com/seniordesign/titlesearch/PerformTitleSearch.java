package com.seniordesign.titlesearch;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
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
@WebServlet(name = "/PerformTitleSearch", urlPatterns={"/", "/search", "/warranty/validate", "/validated"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,
maxFileSize = 1024 * 1024 * 10, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class PerformTitleSearch extends HttpServlet {
	
	TitleSearchManager manager = TitleSearchManager.getInstance();
	
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
				WarrantyDeed deed2 = manager.createWarrantyDeed("WD207-949.pdf");
				manager.addWarrantyDeedToHouseHistory(deed2);
				validateReport(request, response);
				break;
			default:
				break;
		}
	}
	
	protected void getWarrantyDeed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bookNo = request.getParameter("book");
		String pageNo = request.getParameter("page");
		
		System.out.println("Book: " + bookNo + " Page: " + pageNo);
	}

	protected void validateReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		File file = new File(request.getServletContext().getRealPath("") + File.separator + "warrantyDeedPDFs" + File.separator + "WD207-941.pdf");
		List<WarrantyDeed> history = manager.getHouseHistory();
		ServletContext context = this.getServletContext();
		context.setAttribute("history", history);
		request.getRequestDispatcher("/warranty/validate.jsp").forward(request, response);
	}
	
	protected void updateWarrantyDeedText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int index = Integer.parseInt(request.getParameter("index"));
		while(request.getParameter("text" + index) != null) {			
			String modifiedText = request.getParameter("text" + index);
			WarrantyDeed deedToUpdate = manager.getHouseHistory().get(index);
			if(!deedToUpdate.getText().equals(modifiedText)) {				
				System.out.println(modifiedText);
				deedToUpdate.setText(modifiedText);
			}
			index++;
		}
	}
}
