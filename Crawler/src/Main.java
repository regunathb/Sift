/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
*
* @author vimal
*/
public class Main {

	/**
	* @param args the command line arguments
	*/
	public static void main(String[] args)  {
		try {
			
			Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
			Elements newsHeadlines = doc.select("#mp-itn b a");
			System.out.println(newsHeadlines.toString());
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}