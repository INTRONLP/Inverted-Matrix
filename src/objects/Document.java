package objects;

import java.util.ArrayList;
import java.util.List;

import stemming.WordStemmer;

public class Document {
	private String docName;							//filename of the document, will serve as our docID
	private List<String> listOfTerms;				//list of terms found in the document, repeating terms will be counted and will be considered.
	private int id;
	
	public Document(String docName) {
		this.docName = docName;
		this.listOfTerms = new ArrayList<>();
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
	
	public void addTerm(String term) {
		this.listOfTerms.add(term);
	}
	
	public int termCount(String term) {
		int count = 0;
		String stemmedTerm = WordStemmer.stemWord(term);
		
		for(String cmp: listOfTerms) {
			if(stemmedTerm.equalsIgnoreCase(WordStemmer.stemWord(cmp))) {
				count++;
			}
		}
		
		return count;
	}

	public List<String> getTerms() {
		return listOfTerms;
	}
}
