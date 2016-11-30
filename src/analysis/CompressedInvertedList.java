package analysis;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompressedInvertedList implements InvertedList {
    
    private HashMap<String, List<Pair>> map;
    
    public CompressedInvertedList() {
        this.map = new HashMap<>();
    }
    
    @Override
    public void addWord(String word, int document) {
        this.map.putIfAbsent(word, new ArrayList<>());
                
        List<Pair> list = map.get(word);
        
        if (list.isEmpty())
            list.add(new Pair(document, 1));
        else {
            int currentDocument = list.get(0).key;
            
            int i = 0;
            while (currentDocument < document) {
                i++;
                if (i < list.size())
                    currentDocument += list.get(i).key;
                else
                    break;
            }
            
            if (currentDocument == document)
                list.get(i).value++;
            else if (i == list.size()) {
                int doc = document - currentDocument;
                list.add(new Pair(doc, 1));
            }
            else {
                if (i > 0) {
                    document = currentDocument - document;
                    list.add(i, new Pair(document, 1));
                    int nextDocument = list.get(i+1).key - document;
                    list.get(i+1).key = nextDocument;
                }
                else  {
                    list.add(0, new Pair(document, 1));
                    list.get(i+1).key = list.get(i+1).key -  document;
                }
            }  
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
        int documentCount = 0;
        
        for (String word : map.keySet())
            documentCount += map.get(word).size();
        
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




