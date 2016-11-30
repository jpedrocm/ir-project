package analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public interface InvertedList {

    public void addWord(String word, int document);   
    
    public HashMap<Integer, Integer> getWordDocuments(String word);
    
    public int getDocumentCount();
    
    public String toString();   
    
    public void toFile() throws FileNotFoundException, IOException;
}