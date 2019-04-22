package com.seniordesign.titlesearch;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShowPDF
 */
@WebServlet("/ShowPDF")
public class ShowPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowPDF() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub 
		ServletContext context = this.getServletContext();
		@SuppressWarnings("unchecked")
		List<WarrantyDeed> list = null;
		String view = request.getParameter("view");
		if(view.equals("select")) {
			list = (List<WarrantyDeed>) context.getAttribute("multipleResults");
		} else if(view.equals("history")) {
			list = (List<WarrantyDeed>) context.getAttribute("history");
		}
		int index = Integer.parseInt(request.getParameter("deed"));
		WarrantyDeed deed = list.get(index);
		byte[] pdf = deed.getPDF();
		if(pdf != null) {			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline");
			ServletOutputStream output = response.getOutputStream();
			output.write(pdf);
			output.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
