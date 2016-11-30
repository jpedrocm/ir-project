package analysis;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;

public class DefaultInvertedList implements InvertedList {
    
    private HashMap<String, HashMap<Integer, Integer>> map;
    
    public DefaultInvertedList() {
        this.map = new HashMap<String, HashMap<Integer, Integer>>();
    }
    
    public void addWord(String word, int document) {
        this.map.putIfAbsent(word, new HashMap<Integer, Integer>());
        this.map.get(word).putIfAbsent(document, 0);
        this.map.get(word).put(document, this.map.get(word).get(document) + 1);
    }
    
    public HashMap<Integer, Integer> getWordDocuments(String word) {
        return this.map.get(word);
    }
    
    public int getWordDocumentsCount(String word) {
        return map.get(word).size();
    }    
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (String word : this.map.keySet()) {
            sb.append(word + ": ");
            for (Integer document : this.map.get(word).keySet()) {
                sb.append("[" + document + ", " + this.map.get(word).get(document) + "], "); 
            }
            sb.append("\n");
        }        
        
        return sb.toString();
    }
    
    public int getDocumentCount() {
        HashSet<Integer> documents = new HashSet<Integer>();
        
        int count = 0;
        
        for (String word : this.map.keySet())
            for (Integer document : this.map.get(word).keySet())
                if (documents.add(document))
                    count++;
                
        return count;        
    }

    @Override
    public void toFile() throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(("DefaultInvertedList"));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                Writer writer = new BufferedWriter(osw)) {
            
            writer.write(toString());
        }
    }
    
}