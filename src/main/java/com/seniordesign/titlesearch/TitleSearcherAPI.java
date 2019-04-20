package com.seniordesign.titlesearch;

import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder;
import java.util.Scanner;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import org.python.util.PythonInterpreter;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyDictionary;
import com.seniordesign.titlesearch.WarrantyDeed;

public class TitleSearcherAPI {
	private static TitleSearcherAPI singleInstance = null;
	private static ProcessBuilder pb = null;
	private static Process process;
	private static Scanner in;
	private static OutputStream stdin;
	private String imageDir;
	private String pythonPath;
	private String pythonFile;
	private BufferedInputStream fileContent = null;

	private TitleSearcherAPI() {
		//pythonPath = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "WEB-INF" + File.separator + "lib" + File.separator + "python.exe";
		//pythonPath = "/usr/bin/python";
		//pythonPath = "/usr/local/opt/python/libexec/bin/python";
      pythonPath = "/Users/minanarayanan/anaconda2/bin/python";
		pythonFile = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "BeautifulSoupAPI.py";
		imageDir = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "warrantyDeedPDFs";

		try {
			pb = new ProcessBuilder(this.getPythonPath(), this.getPythonFile());
			pb.redirectErrorStream(true);
			process = pb.start();
			in = new Scanner(process.getInputStream());
			stdin = process.getOutputStream();
		} catch (IOException exc) {
			exc.printStackTrace();
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
		} catch (IOException exc) {
			exc.printStackTrace();
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
		} catch (FileNotFoundException exc) {
			exc.printStackTrace();
		}
		return fileContent;
	}

	public List<WarrantyDeed> getPDFWarrantyDeed(String firstNameOrBook, String lastNameOrPage, String county, String prePost) {
		// TODO Change hardcoded state and county values - currently, 0 represents TN
		//			and 0 represents Humphreys county
		Properties properties = System.getProperties();
      properties.put("python.path", this.getPythonPath());
      PythonInterpreter.initialize(System.getProperties(), properties, new String[0]);
		PythonInterpreter python = new PythonInterpreter();
		List<WarrantyDeed> wdList = new ArrayList<WarrantyDeed>();
		try {
			//python.execfile("BeautifulSoupAPI.py"); 
         python.execfile(this.getPythonFile()); 
         PyObject exec = python.get("execute");
         PyObject[] args = {new PyString("w"), new PyString("0"), new PyString("0"), new PyString(this.getImageDirectory()), new PyString("pdf"), new PyString(prePost), new PyString(firstNameOrBook), new PyString(lastNameOrPage), new PyString("0")};
         PyObject deeds = exec.__call__(args);
         for (PyObject deed : deeds.asIterable()) {
				WarrantyDeed wd = new WarrantyDeed();
            PyDictionary deed2 = (PyDictionary) deed;
            List<String> grantors = (List<String>) deed2.get("grantors");
				wd.setGrantors(grantors);
            List<String> grantees = (List<String>) deed2.get("grantees");
				wd.setGrantees(grantees);
            String date = (String) deed2.get("date");
				wd.setTransactionDate(date);
            String pdf = (String) deed2.get("pdf");
            byte[] pdf_1 = pdf.getBytes();
				wd.setPDF(pdf_1);
            String firstArg = (String) deed2.get("firstArg");
				wd.setBookNumber(firstArg);
            String secondArg = (String) deed2.get("secondArg");
				wd.setPageNumber(secondArg);
				wdList.add(wd);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return wdList;
	}

/*
	public static void main(String[] args) {
		TitleSearcherAPI title = TitleSearcherAPI.getInstance();
		List<WarrantyDeed> wd = title.getPDFWarrantyDeed("1", "18", "Humphreys", "0");
	}
*/

}
