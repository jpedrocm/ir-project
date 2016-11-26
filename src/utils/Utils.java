package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {
    
    public static final Path CRAWLED_HTML_PATH = Paths.get("Data", "CrawledHTML");
    
    public static final Path CRAWLED_URLS_PATH = Paths.get("Data", "CrawledURLS");
    
    public static final String DATA_DIRECTORY = "Data";
    
    public static final String SET_PATH = Paths.get("Data", "full_data.arff").toString();

    public static final String MODEL_PATH = Paths.get("Data", "random_forest.model").toString();
    
    public static final String RELEVANT_DOCS_FILE = "RelevantDocs.txt";
    
    public static final String[] INITIAL_URLS = {            
            "http://www.johnlewis.com/",
            "http://www.newegg.com/", 
            "http://www.asus.com/us/", 
            "http://www.microcenter.com/",
            "http://us.toshiba.com/",
            "http://shop.lenovo.com/us",
            "http://www.brandsmartusa.com/",
            "http://www.staples.com/",
            "http://www.pcworld.co.uk/",
            "http://store.hp.com"
    }; 
    
    public static final String[] DOMAIN_URLS = { 
            "http://www.johnlewis.com/",
            "http://www.newegg.com/", 
            "http://www.asus.com", 
            "http://www.microcenter.com/",
            "http://us.toshiba.com/",
            "http://shop.lenovo.com",
            "http://www.brandsmartusa.com/",
            "http://www.staples.com/",
            "http://www.pcworld.co.uk/",            
            "http://store.hp.com"            
    };
    
    public static final String[] DOMAIN = {
            "johnlewis",
            "newegg",
            "asus",
            "microcenter",
            "toshiba",
            "lenovo",
            "brandsmartusa",
            "staples",
            "pcworld",
            "hp"         
    };
    
    public static final String[] MostCommonAttributes = {
            "cpu",
            "screenSize",
            "name",
            "ports",
            "storage"
    };
    
    public static void URLToFile(String URL, Path directory, String filename) throws IOException {
        Document doc = Jsoup.connect(URL).get();        
        
        saveAllToFile(Arrays.asList(doc.html()), directory, filename);
    }

    public static Document fileToDoc(String filepath){
        Document doc = null;
        
        try {
            doc = Jsoup.parse(new File(filepath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return doc;
    }
    
    public static List<String> getFileLines(String filepath) throws FileNotFoundException, IOException {
        List<String> lines = new ArrayList<String>();
        
        try(BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) 
                lines.add(line);
        }
        
        return lines;
    }
    
    public static void createDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public static void saveAllToFile(Iterable<String> strings, Path directory, String filename) throws FileNotFoundException, IOException {
        String filepath = Paths.get(directory.toString(), filename).toString();
        
        createDirectory(directory);
        
        try (FileOutputStream fos = new FileOutputStream((filepath));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                Writer writer = new BufferedWriter(osw)) {
            
            for (String string : strings)
                writer.write(string + "\n");
        }
    }
    
    public static HashMap<String, List<String>> readMapFromFile(String file) throws FileNotFoundException, IOException {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        
        List<String> lines = Utils.getFileLines(file);
        
        for (String line : lines) {
            String[] values = line.split(" ");
            String key = values[0];
            String value = values[1];
            
            if (!map.containsKey(key))
                map.put(key, new ArrayList<String>());
            
            map.get(key).add(value);
        }
        
        return map;
    }
    
    public static void mapToFile(HashMap<String, List<String>> relevantDocs, Path directory, String filename) throws FileNotFoundException, IOException {
        String filepath = Paths.get(directory.toString(), filename).toString();
        
        createDirectory(directory);
        
        StringBuilder sb = new StringBuilder();
        
        try (FileOutputStream fos = new FileOutputStream((filepath));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                Writer writer = new BufferedWriter(osw)) {
            
            for (String key : relevantDocs.keySet())
                for (String value : relevantDocs.get(key))
                    sb.append(key + " " + value + "\n"); 
            
            writer.write(sb.toString());
        }
    }
    
}