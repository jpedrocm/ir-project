package analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;

import classification.Classifier;
import extraction.AbstractWrapper;
import extraction.GeneralExtractor;
import utils.Utils;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        HashMap<String, List<String>> relevantDocs = getClassifiedDocs();
        
        AbstractWrapper generalExtractor = new GeneralExtractor();
        HashMap<String, Integer> specsCount = new HashMap<String, Integer>();
        
        for (String folder : relevantDocs.keySet()) {
            for (String file : relevantDocs.get(folder)) {                
                String filepath = Paths.get(Utils.CRAWLED_HTML_PATH.toString(), folder, file).toString(); 
                
                System.out.println("Extracting file " + filepath);
                
                Document doc = Utils.fileToDoc(filepath);
                
                HashMap<String, List<String>> specs = generalExtractor.getSpecifications(doc);
                for (String key : specs.keySet()) {
                    if (!specsCount.containsKey(key))
                        specsCount.put(key, 0);
                    
                    specsCount.put(key, specsCount.get(key) + 1);
                }
            }
        }
        
        System.out.println("------------------------------------------------------------------");
        
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
        for (String key : specsCount.keySet())
            list.add(new AbstractMap.SimpleEntry<String, Integer>(key, specsCount.get(key)));
        
        list.sort((a, b) -> a.getValue().compareTo(b.getValue()));
        
        for (Entry<String, Integer> entry : list)
            System.out.println(entry.getKey() + ": " + entry.getValue());
    }
    
    private static HashMap<String, List<String>> getClassifiedDocs() throws FileNotFoundException, IOException {
        HashMap<String, List<String>> relevantDocs;
        String relevantDocsPath = Paths.get(Utils.DATA_DIRECTORY, Utils.RELEVANT_DOCS_FILE).toString();
        
        File relevantDocsFile = new File(relevantDocsPath);
        
        if (relevantDocsFile.exists()) {
            relevantDocs = Utils.readMapFromFile(relevantDocsPath);
        }
        else {
            Classifier classifier = new Classifier(); 
            System.out.println("Classifying");
            relevantDocs = classifier.classifyAllDocs(Utils.CRAWLED_HTML_PATH.toString(), true, Utils.SET_PATH, Utils.MODEL_PATH);
            System.out.println("Classified");
            Utils.mapToFile(relevantDocs, Paths.get(Utils.DATA_DIRECTORY), Utils.RELEVANT_DOCS_FILE);
        }
        
        return relevantDocs;
    }
    
    

}
