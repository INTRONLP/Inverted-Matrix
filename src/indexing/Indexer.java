package indexing;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import objects.Document;

import java.util.ArrayList;	

public class Indexer {
	private HashMap<String, List<Document>> indexTable;
	
	public Indexer() {
		indexTable = new HashMap<>();
	}
	
	public Indexer(String directory) {
		loadIndexTable(directory);
	}
	
	/**
	 *  Loads a persistent data storage where an index table has been saved
	 *  
	 *   @param directory	-	the file directory of where the index map is saved
	 */
	public void loadIndexTable(String directory) {
		indexTable = new HashMap<>();
	}
	
	/**
	 *  Saves the indexTable to a persistent storage for later use
	 */
	public void saveIndexTable(String fileName) 
	{
		//TO-DO	: Maybe use XML Files to re-use our XMLReader classes
		FileWriter fileWriter = null;
		
		try{
		fileWriter = new FileWriter(fileName);		 
		
        //Write the CSV file header
        fileWriter.append("term, list_of_documents");     

        
        //Add a new line separator after the header
	    fileWriter.append("\n");
	    
	    Set<String> keys = indexTable.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
		
			fileWriter.append(key);
			List<Document> values = indexTable.get(key);
			
			for(Document doc: values) {
				fileWriter.append(", " + doc.getID());
			}
			fileWriter.append("\n");
			
		}
		fileWriter.close();
		}catch(Exception err) {}
			 
	}
	
	/**
	 * Adds the term and the Mapped Document to the map. 
	 * If the term is already a key in the map, the Mapped Document will be added to the existing list of mapped documents.
	 * If the mapped document is already in the list, it will no longer be added to avoid duplication.
	 * 
	 * @param term		-	The term being saved into the table, if the term is already in the table it adds the docFile to the existing list
	 * @param docFile	-	The filename where the term was found, if the document has been added to the table with the term, it will simply do nothing
	 */
	public void addToIndexTable(String term, Document docFile) {
		ArrayList<Document> docs;					//Will work on making this into a sorted list where the highest counted term is always first.
		
		if(!indexTable.containsKey(term)) {
			docs = new ArrayList<>();
			docs.add(docFile);
			
			indexTable.put(term, docs);
		} else {
			docs = (ArrayList<Document>) indexTable.get(term);
			if(!docs.contains(docFile)) {
				docs.add(docFile);
			}
		}
	}
	
	public void printMap() {
		Set<String> keys = indexTable.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			
			System.out.print(key + " || ");
			List<Document> values = indexTable.get(key);
			
			for(Document doc: values) {
				System.out.print(doc.getDocName() + " || ");
			}
			
			System.out.println("");
		}
	}
	
	public void printResults(String word) {
		List<Document> list = indexTable.get(word);
		
		for(Document doc: list) {
			System.out.print("|| " + doc.getID());
		}
	}
}
