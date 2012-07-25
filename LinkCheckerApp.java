package v4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LinkCheckerApp {

	/**
	 * This application takes url address as first argument. It checks all links of 
	 * this input url if work and images and photos files if exist. 
	 * It also checks all ACTION links of the FORM if work.
	 * The result of all broken links are printed to text files. 
	 * The text file Main_Result(S/L).doc contains all broken links except "broken" Google ones. 
	 * The text file Result(S/L).doc contains all broken links. 
	 * 
	 * @author Marek Dano
	 * @param args URI
	 *  
	 */
	public static void main(String[] args) {
		
		// declare and instantiate print writer
		PrintWriter output = null;
		
		// create object links 
		LinkChecker links = new LinkChecker();
		
		// call getLinks method in LinkChecker class 
		links.getLinks(args[0]);
		
		
		// call getResult method in LinkChecker class and assign 
		// the result to the object list  
		ArrayList<String> list = links.getResult();	
		
		// assign list to string array 
		String[] listLinks = list.toArray(new String[list.size()]);
		
		
		// call getMainResult method in LinkChecker class and
		// assign the result to the object mainList 
		ArrayList<String> mainList= links.getMainResult();
		
		// assign mainList to string array
	    String[] mainListLinks = mainList.toArray(new String[mainList.size()]);
	
		
		// open text file Result(L/S).doc and store all broken links.
	    try {
			FileWriter fw = new FileWriter(new File("ResultS.doc")); // ResultS.doc or ResultL.doc
	        output = new PrintWriter (fw);
		
		} catch(IOException e){
			System.out.println("The text file was not found: " + e.toString() );
	       	System.exit(1);
	    }
		
		// print these lines to text file
		output.println("The result of testing  " + args[0] );
		output.println("List of broken links with google links.\n\n");
		
		
		// print out all broken links to text file 
		for (int i = 0; i<listLinks.length; i++) {
			int j = i+1;
			output.println( j + ".  " + listLinks[i]);
			output.println();
		}
		
		// close file
		output.close();
		
	 
	 
		// open the text file Main_Result(L/S).doc and store broken links except
		// Google links and links contain ftp, mailto, telnet, news and gopher.
		try {
			FileWriter fw = new FileWriter(new File("Main_ResultS.doc")); // Main_ResultS.doc or Main_ResultL.doc
	        output = new PrintWriter (fw);
		
		} catch(IOException e){
			System.out.println("The text file was not found: " + e.toString() );
	       	System.exit(1);
	    }
		
		// print these lines to text file
		output.println("The result of testing  " + args[0] );
		output.println("List of broken links without google links.\n\n");
		
		
		// print out all broken links except Google ones to text file
		for (int i = 0; i<mainListLinks.length; i++) {
			int j = i+1;
			output.println( j + ".  " + mainListLinks[i]);
			output.println();
		}
		
		// close text file
		output.close();
		
	}// end main

} // end linkCheckerApp
