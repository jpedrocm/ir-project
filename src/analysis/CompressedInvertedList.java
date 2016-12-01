package analysis;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CompressedInvertedList implements InvertedList {
    
    private HashMap<String, List<Pair>> map;
    
    public CompressedInvertedList() {
        this.map = new HashMap<>();
    }
    
    @Override
    public void addWord(String word, int document) {
        int debug = document;
        this.map.putIfAbsent(word, new ArrayList<>());
                
        List<Pair> list = map.get(word);
        
        if (list.isEmpty())
            list.add(new Pair(document, 1));
        else {
            for (int i = 0; i <= list.size(); i++) {
                if (i == list.size()) {
                    list.add(new Pair(document, 1)); 
                    break;
                }
                
                Pair pair = list.get(i);
                if (document > pair.key)
                    document -= pair.key;
                else if (document == pair.key) {
                    pair.value++;
                    break;
                }
                else {
                    list.add(i, new Pair(document, 1));
                    pair.key -= document;
                    break;
                }  
                
                i++;
                i--;
            }
            
            int i = 0;
        }        
    }
    
    @Override
    public HashMap<Integer, Integer> getWordDocuments(String word) {
        HashMap<Integer, Integer> documents = new HashMap<>();
        
        int lastDoc = 0;
        for (Pair pair : map.get(word)) {
            documents.put(lastDoc + pair.key, (int) pair.value);
            lastDoc += pair.key;
        }        
        
        return documents;
    }

    @Override
    public int getDocumentCount() {
        HashSet<Integer> documents = new HashSet<Integer>();
        
        int documentCount = 0;
        
        for (String word : this.map.keySet()) {
            int currentDoc = 0;
            
            for (Pair pair : this.map.get(word)) {
                currentDoc += pair.key;
                if (documents.add(currentDoc))
                    documentCount++;
            }
        }            
        
        return documentCount;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (String word : this.map.keySet()) {
            sb.append(word + ": ");
            for (Pair entry : this.map.get(word)) {
                sb.append(entry); 
            }
            sb.append("\n");
        }        
        
        return sb.toString();
    }

    @Override
    public void toFile() throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(("CompressedInvertedList"));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                Writer writer = new BufferedWriter(osw)) {
            
            writer.write(toString());
        }        
    }
    
}




