package token;

import java.util.ArrayList;
import java.util.regex.*;

public class Tokenizer {
	private final static String REGEX_TOKENS = "[A-z]*[0-9]*";//REGEX does not encompass words w/ Special Characters like -' etc. 
	//([A-Z]*[a-z]+[A-z])*([0-9]*)*
	//[A-z]*[0-9]*
	private Pattern pattern;
	private ArrayList<String> tokens;
	
	public Tokenizer(){
		this.tokens = new ArrayList<String>();
		this.pattern = Pattern.compile(REGEX_TOKENS);
	}
	
	public void identifyTokens(ArrayList<String> toTokenize){
		/*The function that will get the tokens from a given file*/
		for(String token : toTokenize){
			Matcher match = this.pattern.matcher(token);
			while(match.find()){
				String tok = match.group();
				tokens.add(tok);
			}	
		}
	}
	
	public void processHyphenatedWords(ArrayList<String> hyphen){
		/*Function that will process words with Hyphen. 
		 * Will be used when REGEX Pattern will not be able to identify them.*/
	}
	
	public ArrayList<String>getAllTokens(){
		return this.tokens;
	}
}
