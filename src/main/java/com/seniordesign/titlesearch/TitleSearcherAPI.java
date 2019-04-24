package com.seniordesign.titlesearch;

import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.core.PyDictionary;
import com.seniordesign.titlesearch.WarrantyDeed;

public class TitleSearcherAPI {
	private static TitleSearcherAPI singleInstance = null;
	private String imageDir;
	private String pythonPath;
	private String pythonFile;
	private BufferedInputStream fileContent = null;

	private TitleSearcherAPI() {
		//pythonPath = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "WEB-INF" + File.separator + "lib" + File.separator + "python.exe";
		//pythonPath = "/usr/bin/python";
		//pythonPath = "C:\\Python27\\python";
		//pythonPath = "/usr/local/opt/python/libexec/bin/python";
		//pythonPath = "/Users/minanarayanan/anaconda2/bin/python";
		pythonFile = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "BeautifulSoupAPI.py";
		imageDir = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "warrantyDeedPDFs";
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
		List<WarrantyDeed> wdList = new ArrayList<WarrantyDeed>();
		Properties properties = System.getProperties();
//		properties.setProperty("python.home", "C:\\liberty\\usr\\servers\\defaultServer\\apps\\myapp.war\\WEB-INF\\lib\\jython-standalone-2.7.1.jar");
//		properties.setProperty("python.path", "C:\\Users\\Jaypt\\eclipse-workspace\\titlesearch\\src\\main\\webapp\\WEB-INF\\lib\\site-packages");
		properties.setProperty("python.home", "/home/vcap/app/wlp/usr/servers/defaultServer/apps/myapp.war/WEB-INF/lib/jython-standalone-2.7.1.jar");
		properties.setProperty("python.path", "/home/vcap/app/wlp/usr/servers/defaultServer/apps/myapp.war/WEB-INF/lib/site-packages");
		try {
			PythonInterpreter.initialize(System.getProperties(), properties, new String[0]);
			PySystemState.initialize();
			PythonInterpreter python = new PythonInterpreter(null, new PySystemState());
			//python.execfile("BeautifulSoupAPI.py"); 
	         python.execfile(this.getPythonFile()); 
	         PyObject exec = python.get("execute");
	         PyObject[] args = {new PyString("w"), new PyString("0"), new PyString("0"), new PyString(this.getImageDirectory()), new PyString("pdf"), new PyString(prePost), new PyString(firstNameOrBook), new PyString(lastNameOrPage), new PyString("0")};
	         PyObject deeds = exec.__call__(args);
	         for (PyObject deed : deeds.asIterable()) {
				WarrantyDeed wd = new WarrantyDeed();
	            PyDictionary deed2 = (PyDictionary) deed;
	            List<String> grantorsList = (List<String>) deed2.get("grantors");
	            String[] grantors = new String[grantorsList.size()];
	            grantorsList.toArray(grantors);
					wd.setGrantors(grantors);
	            List<String> granteesList = (List<String>) deed2.get("grantees");
	            String[] grantees = new String[granteesList.size()];
	            granteesList.toArray(grantees);
					wd.setGrantees(grantees);
	            String date = (String) deed2.get("date");
					wd.setTransactionDate(date);
	//            String pdf = (String) deed2.get("pdf");
	//            System.out.println(deed2.get("pdf").getClass());
	//            
	//				wd.setPDF(pdf_1);
	            String firstArg = (String) deed2.get("firstArg");
					wd.setBookNumber(firstArg);
	            String secondArg = (String) deed2.get("secondArg");
					wd.setPageNumber(secondArg);
					wdList.add(wd);
				String fileName = "WD" + firstArg + "-" + secondArg + ".pdf";
				Path pathToFile = Paths.get(this.getImageDirectory() + File.separator + fileName);
				byte[] buf = Files.readAllBytes(pathToFile);
				wd.setPDF(buf);
				Files.deleteIfExists(pathToFile);
				python.cleanup();
				Thread.sleep(5000);
				System.out.println("Warranty deed obtained. Passing it to Discovery");
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
