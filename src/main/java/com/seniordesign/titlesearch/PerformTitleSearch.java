package com.seniordesign.titlesearch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

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
@WebServlet("/PerformTitleSearch")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,
maxFileSize = 1024 * 1024 * 10, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class PerformTitleSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PerformTitleSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext context = request.getServletContext();
		String action = request.getParameter("action");
		switch(action) {
			case "showWD":
				validateWarrantyDeeds(request, response);
				break;
			case "updateText":
				WarrantyDeed deed = (WarrantyDeed) context.getAttribute("WD");
				String modifiedText = request.getParameter("text");
				deed.setWarrantyDeedText(modifiedText);
				System.out.println(deed.getWarrantyDeedText());
				break;
			default:
				break;
		}
	}

	protected void validateWarrantyDeeds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Part filePart = request.getPart("fileInput");
		String path = filePart.getSubmittedFileName();
		InputStream fileContent = filePart.getInputStream();
		
		int bytesToRead = fileContent.available();
		System.out.println(bytesToRead);
		byte[] pdfInBytes = new byte[bytesToRead];
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int result;
		while((result = fileContent.read(pdfInBytes)) != -1) {
			stream.write(pdfInBytes, 0, result);
		}
		if(result == -1) {
			WarrantyDeed deed = new WarrantyDeed();
			deed.setPDF(stream.toByteArray());
			stream.close();
			deed.setWarrantyDeedText("Hello there");
			ServletContext context = this.getServletContext();
			context.setAttribute("WD", deed);
			request.getRequestDispatcher("/Validate/WarrantyDeed.jsp").forward(request, response);
		}
	}
}
