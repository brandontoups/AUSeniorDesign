//package com.seniordesign.titlesearch;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import com.seniordesign.titlesearch.TitleSearcherAPI;
//
///**
// * Servlet implementation class GetWarrantyDeed
// */
//@WebServlet("/GetWarrantyDeed")
//public class GetWarrantyDeed extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public GetWarrantyDeed() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		String bookNo = request.getParameter("book");
//		String pageNo = request.getParameter("page");
//		try {
//			int intBookNo = Integer.parseInt(bookNo);
//			int intPageNo = Integer.parseInt(pageNo);
//		} catch (NumberFormatException e) {
//			request.setAttribute("statusDesc", "Please enter a valid number.");
//			request.setAttribute("statusCode", "400");
//			request.getRequestDispatcher("/viewWarrantyDeed.jsp").forward(request, response);
//		}
//		String county = "Humphreys";
//		System.out.println(this.getServletContext().getContextPath());
//		
//		// Make sure the folder exists, otherwise BeautifulSoupAPI would not be able to download the file.
//		String uploadPath = this.getServletContext().getRealPath("") + File.separator + "warrantyDeedPDFs";
//		File uploadDir = new File(uploadPath);
//		if(!uploadDir.exists()) {
//			uploadDir.mkdir();
//		}
//		
//		BufferedInputStream fileContent = null;
//		String fileName = "";
//		try {
//			TitleSearcherAPI api = TitleSearcherAPI.getInstance();
//			fileContent = api.getPDFWarrantyDeed(bookNo, pageNo, county);
//			fileName = "WD" + bookNo + "-" + pageNo + ".pdf";
//			if(fileContent == null) {
//				request.setAttribute("status", "File could not be downloaded from the website.");
//				request.setAttribute("errorCode", "503");
//				request.getRequestDispatcher("/viewWarrantyDeed.jsp").forward(request, response);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			request.setAttribute("status", "File could not be downloaded from the website.");
//			request.getRequestDispatcher("/viewWarrantyDeed.jsp").forward(request, response);
//		}
//		
//		
//		if(fileContent != null) {			
//			try {
//				BoxAPI boxAPI = new BoxAPI(Folder.WARRANTYPDF);
//				String fileFolder = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "warrantyDeedPDFs";
//				File pdfFileToUpload = new File(fileFolder + File.separator + fileName);
//				
//				/* I have to create a new bufferedinputstream by opening the file again because Discovery was having difficulty processing the index
//					of the document. The problem is that I upload the pdf file first to Box using the API which reads the entire
//					bufferedinputstream and when this stream was also passed to Discovery, it didn't have anything to read so it would throw an internal error. */
//				BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(pdfFileToUpload));
//				if(pdfFileToUpload.exists()) {					
//					boxAPI.uploadFile(inputStream, fileName);
//					inputStream.close();
//					if(pdfFileToUpload.delete()) {
//						System.out.println("File deleted: " + fileName);
//					}
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				request.setAttribute("statusDesc", "File could not be downloaded from the website.");
//				request.setAttribute("statusCode", "503");
//				request.getRequestDispatcher("/viewWarrantyDeed.jsp").forward(request, response);
//			}
//			
//			// Pass the inputstream to the UploadFile Servlet to handle the upload to discovery and the .txt file to Box.
//			request.setAttribute("fileContent", fileContent);
//			request.setAttribute("fileName", fileName);
//			request.setAttribute("fileContentType", "APPLICATION_PDF");
//			request.getRequestDispatcher("/UploadFile").include(request, response);
//			request.setAttribute("statusDesc", "File downloaded successfully.");
//			request.setAttribute("statusCode", "200");
//			request.getRequestDispatcher("/viewWarrantyDeed.jsp").forward(request, response);
//		}
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//	}
//
//}
