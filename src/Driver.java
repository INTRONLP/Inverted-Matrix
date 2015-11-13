import java.io.File;
import java.util.List;

import doc.DocumentReader;
import indexing.Indexer;
import objects.Document;
import stemming.WordStemmer;

public class Driver {

	public static void main(String[] args) {
		String directory = "Filipino Texts";
		String word = WordStemmer.stemWord("Maraming konsyumer");
		
		File folder = new File(directory);
		Indexer indexer = new Indexer();
		
		addToIndex(folder, indexer);
		
//		indexer.printMap();
		indexer.saveIndexTable("index table.csv");
		System.out.print(word);
		indexer.printResults(word);
	}
	
	private static int id = 0;
	
	public static void addToIndex(File file, Indexer indexer) {
		File[] listOfFiles = file.listFiles();
		
		for(File f: listOfFiles) {
			if(!f.isDirectory()) {
				Document doc = DocumentReader.readDocument(f.getPath());
				doc.setID(id);
				id++;
				List<String> listOfTerms = doc.getTerms();
				
				for(String term: listOfTerms) {
					indexer.addToIndexTable(term, doc);
				}
			} else {
				addToIndex(f, indexer);
			}
		}
	}
}
