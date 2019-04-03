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
	private static final int MAXGRANTORS = 100;
	private static final int MAXGRANTEES = 100;
	private static final int MAXDEEDS = 100;

	private TitleSearcherAPI() {
		//pythonPath = new File("").getAbsolutePath() + File.separator + "apps" + File.separator + "myapp.war" + File.separator + "WEB-INF" + File.separator + "lib" + File.separator + "python.exe";
		//pythonPath = "/usr/bin/python";
		pythonPath = "/Users/minanarayanan/anaconda2/bin/python";
		pythonFile = new File("").getAbsolutePath() + File.separator + "apps";
		pythonFile += File.separator + "myapp.war" + File.separator + "BeautifulSoupAPI.py";
		imageDir = new File("").getAbsolutePath() + File.separator + "apps" + File.separator
		imageDir += "myapp.war" + File.separator + "warrantyDeedPDFs";
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

	public WarrantyDeed[] getPDFWarrantyDeedfirstNameOrBook(String firstNameOrBook, String lastNameOrPage, String county, String prePost) {
		// TODO Change hardcoded state and county values - currently, 0 represents TN
		//			and 0 represents Humphreys county
		WarrantyDeed wd = new WarrantyDeed();
		WarrantyDeed[] wdList = new WarrantyDeed[MAXDEEDS];
		String[] grantorsList = new String[MAXGRANTORS];
		String[] granteesList = new String[MAXGRANTEES];
		String fileName = "WD" + firstNameOrBook + "-" + lastNameOrPage + ".pdf";
		String command = "w 0 0 " + this.getImageDirectory() + " pdf " + prePost;
		int listIndex = 0;
		command += " " + firstNameOrBook + " " + lastNameOrPage + " 0\n";
		try {
			String line;
			if(stdin != null) {
				stdin.write(command.getBytes());
				stdin.flush();
				System.out.println(command);
			}
			while((in.hasNext()) && (in != null)) {
				line = in.nextLine();
				if (line.trim().equals("Date:"))
				{
					wd.setTransactionDate(in.nextLine());
				}
				if (line.trim().equals("Grantors:"))
				{
					line = in.nextLine();
					int grantorsIndex = 0;
					while (!(line.trim().equals("Grantees:")))
					{
                  if (!(line.trim().isEmpty()))
                  {
                     grantorsList[grantorsIndex] = line;
                     grantorsIndex += 1;
                  }
						line = in.nextLine();
					}
				}
				if (line.trim().equals("Grantees:"))
				{
					line = in.nextLine();
					granteesList[0] = line;
					int granteesIndex = 1;
					while (in.hasNext())
					{
						line = in.nextLine();
						if (!(line.trim().isEmpty()))
						{
							granteesList[granteesIndex] = line;
							granteesIndex += 1;
						}
					}
					wd.setGrantors(grantorsList);
					wd.setGrantees(granteesList);
					Path pathToFile = Paths.get(this.getImageDirectory() + File.separator + fileName);
					if (pathToFile.exists()) {
						try {
							byte[] buf = Files.readAllBytes(pathToFile);
							wd.setPDF(buf);
							wd.setBookNumber(firstNameOrBook);
							wd.setPageNumber(lastNameOrPage);
							wdList[listIndex] = wd;
							listIndex += 1;
							fileName = "WD" + firstNameOrBook + "-" + lastNameOrPage + "_" + Integer.toString(listIndex) + ".pdf";
						} catch (IOException exc) {
							exc.printStackTrace();
						}
					}
					wd = new WarrantyDeed();
				}
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		return wdList;
	}

   /*
	public static void main(String[] args) {
		TitleSearcherAPI title = TitleSearcherAPI.getInstance();
		WarrantyDeed wd = title.getPDFWarrantyDeedfirstNameOrBook("18", "21", "Humphreys", "0");
		System.out.println(wd.getBookNumber());
	}
   */
