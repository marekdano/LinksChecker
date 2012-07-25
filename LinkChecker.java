package v4;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * This class contains get methods for getting instance variables 
 * result and mainResult. It also contains getLinks method to check 
 * if links work. If they do not work, they are added to lists
 * (result, mainResult). 
 * 
 * @author Marek Dano
 *
 */
public class LinkChecker {

	private ArrayList<String> result;
	private ArrayList<String> mainResult;
	private URL url;
	
		
	// ----------------------------- methods --------------------------
	
	public ArrayList<String> getResult(){
		return result;
	}
	
	public ArrayList<String> getMainResult(){
		return mainResult;
	}
	
	/**
	 *  This method takes as a parameter an URI (e.g. http://host.com/page.html).
	 *  It checks all links, image files and forms links in HTML document. It adds all
	 *  broken links to instance variables result and mainResult. The result contains 
	 *  all broken links. The mainResult contains all broken links except 
	 *  "broken" Google ones and links contain ftp, mailto, telnet, news and gopher. 
	 *  
	 * @param URI
	 */ 
	public void getLinks(String uri) {
	
		// instantiate result of ArrayList
	    result = new ArrayList<String>();
	    // instantiate result of ArrayList
	    mainResult = new ArrayList<String>();
	    
	    
	    try {
	    
	    	// instantiate url
	    	url = new URI(uri).toURL();
	        // get connection to url 
	        URLConnection conn = url.openConnection();
	        	        	               
	        // Create a reader on the HTML content
	        Reader rd = new InputStreamReader(conn.getInputStream());
	            
	        // Parse the HTML
	        HTMLEditorKit kit = new HTMLEditorKit();     
	        HTMLDocument doc = (HTMLDocument)(kit.createDefaultDocument());
	        
	        //set a property on the document to ignore charset directive.
	        doc.putProperty("IgnoreCharsetDirective", new Boolean(true));  	        
	        
	        kit.read(rd, doc, 0);
	             
	        // Find all the A tag elements in the HTML document
	        // iterator reads the A tags
	        HTMLDocument.Iterator itA = doc.getIterator(HTML.Tag.A);
	        	        
	        // loop to check all links in HTML document
	        while (itA.isValid()) {
	        
	        	// get all attributes of A tag	            
	        	SimpleAttributeSet setA = (SimpleAttributeSet)itA.getAttributes();
	        	System.out.println("the link attribute set: " + setA);
	            
	        	// get HREF attribute (e.g. "subdir/file.html" or "../index.html" or
	        	// #location or file.html#location or pic.jpg)
	            String linkA = (String)setA.getAttribute(HTML.Attribute.HREF);
	            System.out.println( "The link: " + linkA);
      
	            if (linkA != null) {    
	                // sub-link in the HTML document
	            	// call private method getLink to check if linkA works 
	    	    	getLink(linkA);
	             }
	            // read next A tag
	            itA.next();
	    	}
	        
	        // Find all the IMG tag elements in the HTML document
	        // iterator reads IMG tags
	        HTMLDocument.Iterator itImg = doc.getIterator(HTML.Tag.IMG);
	        	        	        
	        // loop to check all image files in HTML document
	        while (itImg.isValid()) {
	        	
	        	// get all attributes of IMG tag 
	        	AttributeSet set = itImg.getAttributes();
	            
	           	// get SRC attribute
	            String linkImg = (String)set.getAttribute(HTML.Attribute.SRC);
	            System.out.println( "The link: " + linkImg);
	        	        	        
	            if (linkImg != null) {    
	            	// call private method getLinkImg to check linkImg if works
	            	// if image file exist 
	    	    	getLinkImg(linkImg);
	            }
	            // read next IMG tag
	            itImg.next();
	        }

	        // Find all the FORM tag elements in the HTML document through INPUT tag
	        // iterator reads INPUT tags
	        HTMLDocument.Iterator itInput = doc.getIterator(HTML.Tag.INPUT);
	        	        
	        // loop to check all FORM ACTION links through INPUT tag in HTML document	                  
	        while (itInput.isValid()){
	        	
	        	// get all attributes of INPUT tag 
	        	AttributeSet setForm = itInput.getAttributes();
	            
	        	// get ACTION attribute	
	            String linkForm = (String)setForm.getAttribute(HTML.Attribute.ACTION);
	            System.out.println("The link: " + linkForm);
	        
	            if (linkForm != null) {    
	            	// call private method getLinkForm to check link works in FORM tag.
	    	    	getLinkForm(linkForm);
	            }
	            // read next INPUT tag
	            itInput.next();
	        }
	       
	    } catch (MalformedURLException e) { 
	    	System.out.println("bad formed url: " + e.toString());
	    	
	    } catch (URISyntaxException e) { 
	    	System.out.println("url syntax error: " + e.toString());
	    	
	    } catch (BadLocationException e) { 
	    	System.out.println("bad location: " + e.toString());
	    	
	    } catch (IOException e) { 
	    	System.out.println("error io: " + e.toString());
	    	
	    } catch (RuntimeException e) { 
	    	System.out.println("error runtime: " + e.toString());
	    } 	
	        
	}// end getLinks method
	
	/**
	 * This method checks the link in HTML document. The broken link is added to instance 
	 * variables result or mainResult. The result contains all broken links. The mainResult
	 * contains all broken links except "broken" Google ones and links contain 
	 * ftp, mailto, telnet, news and gopher.
	 *  
	 * @param link - URI
	 */
	private void getLink (String link){
		
		// sub-link in the html document
    	try {
    	
    		// local variable url to create url (e.g. http://host.com/page.html)
    		URL urlLink = new URL(url,link);
    		
    		// get connection to url
    		URLConnection connLink = urlLink.openConnection();
    		    		
    		// set connecting overtime to get runtime error
    		// this time in milliseconds can be changed
    		connLink.setConnectTimeout (30000);
    		// read operation overtime  
    		connLink.setReadTimeout (30000);
    		
    		// get first header 
    		String value = connLink.getHeaderField(0);
    		    		   		
    		// get a code of the connection
    		int code=((HttpURLConnection) connLink).getResponseCode();
    		    			
    		String urlSource = urlLink.toString();
    	
    		if (code != 200) {
    		
    			// add the broken link to result
    			result.add(urlSource + "   >>>>>>  " + value);
    			
    			
    			// add all broken links except Google ones 
       			// to mainResult
    			if (!urlSource.contains("google")) {
 
    				mainResult.add(urlSource + "   >>>>>>  " + value);
    			}
       		}
    	
    	} catch (MalformedURLException e) {
    		
    		System.out.println("Bad Formed URL: " + e.toString());
    		
    		// add link to result
    		result.add(link + "   >>>>>>  Bad Formed URL Error:  " + e.toString());
	    	
	    	
    		// do not add broken links if contains ftp, mailto, telnet,
	    	// news and gopher to the mainResult
	    	if (!link.contains("ftp") && !link.contains("mailto") &&
	    		!link.contains("telnet") && !link.contains("news") &&
	    		!link.contains("gopher") ) {
	    		
	    		// add link to main result
	    		mainResult.add(link + "   >>>>>>  Bad Formed URL Error:  " + e.toString());
	      	}	    	
    		    
    	} catch (IOException e) { 
	       	
    		System.out.println("IO Error: " + e.toString());
	    	
    		// add link to result
	    	result.add(link + "   >>>>>>  IO Error:  " + e.toString());
	        	
	    	
	    	// do not add broken links if contains ftp, mailto, telnet,
	    	// news and gopher to the mainResult
	    	if (!link.contains("ftp") && !link.contains("mailto") &&
		    		!link.contains("telnet") && !link.contains("news") &&
		    		!link.contains("gopher") ) {
	    		
	    	
	    		// add link to result
	    		mainResult.add(link + "   >>>>>>  IO Error:  " + e.toString());
	    	}
	    	
    	} catch (RuntimeException e) { 
	       	
    		System.out.println("Runtime Error: " + e.toString());
	    	
	       	// add link to result
	    	result.add(link + "   >>>>>>  Runtime Error:  " + e.toString());
	    	    	
	    	
	    	// do not add broken links if contains ftp, mailto, telnet,
	    	// news and gopher to the mainResult
	    	if (!link.contains("ftp") && !link.contains("mailto") &&
		    		!link.contains("telnet") && !link.contains("news") &&
		    		!link.contains("gopher") ) {
	    	
	    		// add link to mainResult
	    		mainResult.add(link + "   >>>>>>  Runtime Error:  " + e.toString());
	    	}
	    } 	
	}// end getLink method
	
	/**
	 * This method checks image files in HTML document. If they do not exist,
	 * they are added to instance variables result and mainResult.
	 *  
	 * @param link - URI
	 */
	private void getLinkImg(String linkImg) {
		
		try {
			
			// local variable url to create url (e.g. http://host.com/page.html)
    		URL urlLink = new URL(url,linkImg);
    		 		
    		// get the URL connection
    		URLConnection connLink = urlLink.openConnection();
    		    		
    		// set connecting overtime
    		connLink.setConnectTimeout (30000);
    		// read operation overtime  
    		connLink.setReadTimeout (30000);
    		
    		// get first header
    		String value = connLink.getHeaderField(0);
    		    		
    		// get a code of the connection
    		int code = ((HttpURLConnection) connLink).getResponseCode();
    		
    		String urlSource = urlLink.toString();
    		
    		if (code != 200){
    		
    			// add a broken link
    			result.add(urlSource + "   >>>>>>  " + value);
    			mainResult.add(urlSource + "   >>>>>>  " + value);
	   		}
        	
		} catch (MalformedURLException e) {
    		
			System.out.println("Bad Formed URL: " + e.toString());
    		
			// add a broken link
	    	result.add(linkImg + "   >>>>>>  Bad Formed URL Error:  " + e.toString());
	    	mainResult.add(linkImg + "   >>>>>>  Bad Formed URL Error:  " + e.toString());
	    		    	   		    
    	} catch (IOException e) { 
	       	
    		System.out.println("IO Error: " + e.toString());
	       	
    		// add a broken link
	    	result.add(linkImg + "   >>>>>>  IO Error:  " + e.toString());
	    	mainResult.add(linkImg + "   >>>>>>  IO Error: " + e.toString());
    		    	
    	} catch (RuntimeException e) { 
	       	
    		System.out.println("Runtime error: " + e.toString());
	       	
    		// add a broken link
	    	result.add(linkImg + "   >>>>>>  Runtime Error");
	    	mainResult.add(linkImg + "   >>>>>>  Runtime Error");
	    }	
		
	}// end getLinkImg method
	
	/**
	 * This method checks the FORM ACTION link in HTML document. The broken link 
	 * is added to instance variables result and mainResult.
	 *  
	 * @param link - URI
	 */
	private void getLinkForm(String linkForm) {
		
		try {
			
			// local variable url to create url (e.g. http://host.com/page.html)
    		URL urlLink = new URL(url,linkForm);
    		
    		// get the URL connect
    		URLConnection connToLink = urlLink.openConnection();
    		
    		// set connecting overtime
    		connToLink.setConnectTimeout (30000);
    		// read operation overtime  
    		connToLink.setReadTimeout (30000);
    		
    		// get first header
    		String value = connToLink.getHeaderField(0);
        		
    		// get the code of connection
    		int code = ((HttpURLConnection) connToLink).getResponseCode();
    		    	
    		String urlSource = urlLink.toString();
    		
    		if (code != 200){
    		
    			// add a broken link
    			result.add(urlSource + "   >>>>>>  " + value);
    			mainResult.add(urlSource + "   >>>>>>  " + value);
    		}	
    		
    	} catch (MalformedURLException e) { 
    		
    		System.out.println("Bad Formed URL: " + e.toString());
    		
    		// add a broken link
    		result.add(linkForm + "   >>>>>>  Bad Formed URL Error: " + e.toString());
	       	mainResult.add(linkForm + "   >>>>>>  Bad Formed URL Error: " + e.toString());
	    	    
    	} catch (IOException e) { 
    		
    		System.out.println("IO Error: " + e.toString());
    		
    		// add a broken link
    		result.add(linkForm + "   >>>>>>  IO Error:  " + e.toString());
	    	mainResult.add(linkForm + "   >>>>>>  IO Error:  " + e.toString());
	    
	    } catch (RuntimeException e) { 
	    
	    	System.out.println("Runtime Error: " + e.toString());
	    	
	    	// add a broken link
	    	result.add(linkForm + "   >>>>>>  IO Error:  " + e.toString());
	    	mainResult.add(linkForm + "   >>>>>>  IO Error:  " + e.toString());
	    } 	
		
	} // end getLinkForm method

}// end LinkChecker class
