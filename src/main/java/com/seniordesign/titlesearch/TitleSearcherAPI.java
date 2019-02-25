package com.seniordesign.titlesearch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder;
import java.util.Scanner;

public class TitleSearcherAPI {
	private static TitleSearcherAPI singleInstance = null;
	private static ProcessBuilder pb = null;
	private static Process process;
	private static Scanner in;
	private static Scanner err;
	private static OutputStream stdin;
	private String imageDir;
	private String pythonPath;
	private String pythonFile;
	private BufferedInputStream fileContent = null;
	
	private TitleSearcherAPI() {
		//pythonPath = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "WEB-INF" + File.separator + "lib" + File.separator + "python.exe";
		pythonPath = "/usr/bin/python";
		pythonFile = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "BeautifulSoupAPI.py";
		imageDir = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "warrantyDeedPDFs";
		try {
			pb = new ProcessBuilder(this.getPythonPath(), this.getPythonFile());
			pb.redirectErrorStream(true);
			process = pb.start();
			in = new Scanner(process.getInputStream());
			stdin = process.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(pythonPath);
	}
	
	public static TitleSearcherAPI getInstance() {
		if(singleInstance == null) {
			singleInstance = new TitleSearcherAPI();
		}
		return singleInstance;
	}
	
	public void closeInputStream() {
		try {
			this.fileContent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getImageDirectory() {
		return this.imageDir;
	}
	
	private String getPythonFile() {
		return this.pythonFile;
	}
	
	private String getPythonPath() {
		return this.pythonPath;
	}
	
	private BufferedInputStream returnInputStreamOfFile(String fileName) {
		File downloadedFile = new File(this.getImageDirectory() + File.separator + fileName);
		if(!downloadedFile.exists()) {
			System.out.println("File failed to download.");
			return fileContent;
		}
		System.out.println("File downloaded");
		try {
			fileContent = new BufferedInputStream(new FileInputStream(downloadedFile));	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent;
	}
	
	public BufferedInputStream getPDFWarrantyDeed(String bookNo, String pageNo, String county) {
		String command = "w " + bookNo + " " + pageNo + " " + county + " " + this.getImageDirectory() + " pdf" + "\n";	
		try {
			String line;
			if(stdin != null) {
				stdin.write(command.getBytes());
				stdin.flush();
				System.out.println(command);
			}
			while(in.hasNext() || err.hasNext()) {
				if(in.hasNext()) {					
					line = in.nextLine();
				} else {
					line = err.nextLine();
				}
				System.out.println(line);
				/* Since the python script is running constantly to keep a connection the website to avoid login quota timeouts.
					I added a print("--EOF--") in the file in order to know when to exit this function, otherwise we would be just
					waiting for the script to finish exiting (which never will). */
				if(line.equals("--EOF--")) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName = "WD" + bookNo + "-" + pageNo + ".pdf";		
		BufferedInputStream fileContent = returnInputStreamOfFile(fileName);
		return fileContent;
	}
}
