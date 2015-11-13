package doc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import objects.Document;
import stemming.WordStemmer;

/**
 * 
 * @author Miko
 * 
 * Reads a document and gets terms from docu while removing stopper words.
 *
 */
public class DocumentReader {
	public static final String[] stopperWords = {"akin", "aking", "ako", "akong", "alin", 
			"aling", "amin", "aming", "ang", "ano", "anong", "at", "atin", "ating", "ay",  
			"ayan", "ayon", "ayun", "dahil", "daw", "di", "din", "dito", "eto", "ganito",  
			"ganiyan", "ganon", "ganoon", "ganyan", "hayan", "hayun", "heto", "hindi",  
			"ikaw", "inyo", "ito", "iyan", "iyo", "iyon", "ka", "kami", "kanila", "kaniya", "kapag", 
			"kasi", "kay", "kayo", "kina", "ko", "kung", "lang","mag", "maging", "mang", "may", "mga", 
			"mo", "mong", "na", "namin", "natin", "ng", "nga", "nga", "ngunit", "nila", "ninyo", "nito", 
			"niya", "niyon", "nya", "nyo", "nyon", "pa", "pag", "pala", "para", "pati","po", "sa", "saan", 
			"saka", "samin", "san", "sapagkat", "si", "sila", "sino", "siya", "subalit", "sya", "tayo", 
			"tungkol", "u","ung", "upang", "yan", "yun", "yung" };
	
	public static Document readDocument(String filename) {
		Document doc = new Document(filename);
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line =  br.readLine();
			
			while(line != null) {
				String[] words = line.split(" ");
				for(String token: words) {
					if(!isStopperWord(token)) {
						doc.addTerm(WordStemmer.stemWord(removePunctuationAndNumber(token)));
					}
				}
				line = br.readLine();
			}
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	
	public static boolean isStopperWord(String word) {
		for(String stop: stopperWords) {
			if(stop.equalsIgnoreCase(word))
				return true;
		}
		
		return false;
	}
	
	public static String removePunctuationAndNumber(String word) {
		char[] temp = word.toCharArray();
		String ret = "";
		
		for(char c: temp) {
			if(Character.isAlphabetic(c)) {
				ret += "" + c;
			}
		}
		
		return ret;
	}
}
