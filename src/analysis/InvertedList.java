package analysis;

import java.util.HashMap;

public class InvertedList {

    private HashMap<String, HashMap<Integer, Integer>> map;
    
    public InvertedList() {
        this.map = new HashMap<String, HashMap<Integer, Integer>>();
    }
    
    public void addWord(String word, int document) {
        this.map.putIfAbsent(word, new HashMap<Integer, Integer>());        
        this.map.get(word).putIfAbsent(document, 0);
        this.map.get(word).put(document, this.map.get(word).get(document) + 1);
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
    
}