package analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.nodes.Document;

import classification.Classifier;
import extraction.AbstractWrapper;
import extraction.GeneralExtractor;
import utils.Utils;

public class Main {

    private static final String[] attributesString = new String[] {
            "cpu",
            "name",
            "ports",
            "screenSize",
            "storage"
    };

    public static void main(String[] args) throws FileNotFoundException, IOException {
        HashMap<String, List<String>> relevantDocs = getClassifiedDocs();

        AbstractWrapper generalExtractor = new GeneralExtractor();
        HashMap<String, Integer> specsCount = new HashMap<String, Integer>();
        
        HashMap<String, HashSet<String>> attributes = new HashMap<>();
        for (String s : attributesString)
            addAttributes(s, attributes);
        
        InvertedList invertedList = new InvertedList();

        for (String folder : relevantDocs.keySet()) {
            for (String file : relevantDocs.get(folder)) {                
                String filepath = Paths.get(Utils.CRAWLED_HTML_PATH.toString(), folder, file).toString(); 

                System.out.println("Extracting file " + filepath);

                Document doc = Utils.fileToDoc(filepath);

                HashMap<String, List<String>> specs = generalExtractor.getSpecifications(doc);  
                filterSpecs(specs, attributes.keySet());
                
                for (String key : specs.keySet())
                    for (String value : specs.get(key)) {
                        value = value.toLowerCase();
                        
                        String[] tokens = value.split(" |\\+|\\@|\\#|\\=|\\%|\\)|\\(|\\:|\\;|\\,|<|>|\\}|\\{|\\/|[/]|\\*");
                        
                        for (String token : tokens) {
                            for (String attr : attributes.get(key)) {
                                String word = attr + "." + token;
                                invertedList.addWord(word, Integer.parseInt(file));
                            }                            
                        }                        
                    }
            }
        }              
        
        
        try (FileOutputStream fos = new FileOutputStream(("invertedList.txt"));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                Writer writer = new BufferedWriter(osw)) {
            
            writer.write(invertedList.toString());
        }
    }
    
    private static void filterSpecs(HashMap<String, List<String>> specs, Set<String> filter) {  
        Set<String> keySet = new HashSet<String>();
        for (String key : specs.keySet())
            keySet.add(key);
        
        for (String key : keySet) { 
            if (!filter.contains(key))
                specs.remove(key);
        }
    }

    private static void addAttributes(String attribute, HashMap<String, HashSet<String>> map) throws FileNotFoundException, IOException {     
        List<String> lines = Utils.getFileLines(Paths.get(Utils.DATA_DIRECTORY, attribute + "Attributes.txt").toString());

        for (String line : lines) {
            if (!map.containsKey(line)) 
                map.put(line, new HashSet<String>());
                
            map.get(line).add(attribute);            
        }
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

    private static void renameFiles() throws IOException {
        int fileCount = 0;

        File directory = new File(Utils.CRAWLED_HTML_PATH.toString());
        for (File folder : directory.listFiles()) {
            for (int i = 0; i < 1000; i++) {
                File file = new File(Paths.get(folder.getPath(), String.valueOf(i)).toString());
                if (file.exists()) {
                    Path source = Paths.get(file.getPath());   
                    Files.move(source, source.resolveSibling(fileCount + ""), StandardCopyOption.REPLACE_EXISTING);
                }

                fileCount++;
            }               
        }        
    }



}
