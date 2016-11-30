package analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import utils.Utils;

public class Main {
    
    public static Map<String, String> console() {
        Map<String, String> query = new HashMap<>();
        
        try (Scanner in = new Scanner(System.in)) {
            System.out.println(Utils.ATTR_CPU);
            String cpu = in.nextLine();     
            if (!cpu.isEmpty())
                query.put(Utils.ATTR_CPU, cpu);
            
            System.out.println(Utils.ATTR_NAME);
            String name = in.nextLine();
            if (!name.isEmpty())
                query.put(Utils.ATTR_NAME, name);
            
            System.out.println(Utils.ATTR_PORTS);
            if (!name.isEmpty())
                query.put(Utils.ATTR_NAME, name);
            String ports = in.nextLine();
            
            System.out.println(Utils.ATTR_SCREEN);
            String screen = in.nextLine();        
            if (!screen.isEmpty())
                query.put(Utils.ATTR_SCREEN, screen);
            
            System.out.println(Utils.ATTR_STORAGE);
            String storage = in.nextLine();
            if (!storage.isEmpty())
                query.put(Utils.ATTR_STORAGE, storage);
        }         
        
        return query;
    }
    
    private static boolean getIsBoolean() {
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("1 - normal, 2 - boolean");
            return in.nextInt() == 2;
        }
    }
    
    private static int getTopN() {
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("TopN");
            return in.nextInt();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {        
        System.out.println("Creating inverted list...");                
        InvertedList invertedList = createInvertedList();        
        System.out.println("Inverted list created");
        
        invertedList.toFile();
        
        Map<String, String> query = console();
        boolean isBoolean = getIsBoolean();
        int topN = getTopN();
        
        List<Pair> scores = calculateDocumentScores(invertedList, query, true);
        
        System.out.println(scores);
    }

    private static List<Pair> calculateDocumentScores(InvertedList invertedList, Map<String, String> query, boolean isBoolean) {
        HashMap<Integer, Double> scores = new HashMap<>();        
        
        List<String> queryWords = new ArrayList<String>();
        for (String queryAttr : query.keySet())
            for (String caseFold : caseFolding(query.get(queryAttr)))
                queryWords.add(queryAttr + "." + caseFold);
        
        HashMap<String, Double> queryWeights = calculateQueryWeights(queryWords, invertedList, isBoolean);
        
        double normalizerQuery = 0;
        for (Double value : queryWeights.values())
            normalizerQuery += value * value;
        
        int documentsCount = invertedList.getDocumentCount();
        
        HashMap<Integer, Double> normalizerDoc = new HashMap<>();
        
        for (String queryWord : queryWords) {
            HashMap<Integer, Integer> documents = invertedList.getWordDocuments(queryWord);
            
            for (Integer document : documents.keySet()) {
                scores.putIfAbsent(document, 0.0);
                double documentTermWeight;
                if (isBoolean)
                    documentTermWeight = 1;
                else
                    documentTermWeight = documents.get(document) * Math.log(documentsCount / documents.size());
                
                scores.put(document, scores.get(document) + (documentTermWeight * queryWeights.get(queryWord)));
                
                normalizerDoc.putIfAbsent(document, 0.0);
                normalizerDoc.put(document, normalizerDoc.get(document) + (documentTermWeight * documentTermWeight));
            }
        }       
        
        List<Pair> scoresList = new ArrayList<Pair>();
        for (Integer document : scores.keySet())
            scoresList.add(new Pair(document, scores.get(document) / (Math.sqrt(normalizerDoc.get(document)) * Math.sqrt(normalizerQuery))));
        
        scoresList.sort((Pair p1, Pair p2) -> Double.compare(p2.value, p1.value));
        
        return scoresList;
    }
    
    private static InvertedList createInvertedList() throws FileNotFoundException, IOException {
        InvertedList invertedList = new CompressedInvertedList();
        
        HashMap<String, HashSet<String>> attributes = new HashMap<>();
        for (String s : Utils.MOST_COMMON_ATTRIBUTES)
            addAttributes(s, attributes);
        
        List<String> specsFromFile = Utils.getFileLines(Paths.get(Utils.DATA_DIRECTORY, Utils.MOST_COMMON_SPECS_FILE).toString());
        for (String line : specsFromFile) {
            String[] values = line.split(Utils.COMMON_SPECS_SEPARATOR);
            String key = values[0];
            String value = values[1];
            String file = values[2];
            
            String[] tokens = caseFolding(value);            
            for (String token : tokens) {
                for (String attr : attributes.get(key)) {
                    String word = attr + "." + token;
                    invertedList.addWord(word, Integer.parseInt(file));
                }                            
            }
        }
        
        return invertedList;
    }
    
    private static void toFile(HashMap<String, List<String>> specs, String file) throws FileNotFoundException, IOException {        
        try (FileOutputStream fos = new FileOutputStream(("mostCommonSpecs.txt"), true);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                Writer writer = new BufferedWriter(osw)) {
            
            for (String spec : specs.keySet())
                for (String value : specs.get(spec))
                    writer.write(spec + "`" + value + "`" + file + "\n"); 
        }
    }
    
    private static HashMap<String, Double> calculateQueryWeights(List<String> query, InvertedList invertedList, boolean isBoolean) {
        HashMap<String, Double> queryWeights = new HashMap<String, Double>();
        
        if (isBoolean) {
            for (String word : query)
                queryWeights.put(word, 1.0);
            
            return queryWeights;
        }            
        
        double maxFrequency = 0;        
        for (String word : query) {
            if (!queryWeights.containsKey(word))
                queryWeights.put(word, 1.0);
            else
                queryWeights.put(word, queryWeights.get(word) + 1);
            
            maxFrequency = Math.max(maxFrequency, queryWeights.get(word));
        }
        
        int documentsCount = invertedList.getDocumentCount();
        for (String word : queryWeights.keySet()) {
            double wordFrequency = queryWeights.get(word);
            
            double idf = Math.log(documentsCount / queryWeights.get(word));
            
            queryWeights.put(word, (0.5 + (0.5 * ((double) wordFrequency / maxFrequency))) * idf);            
        }       
        
        return queryWeights;
    }
    
    
    private static List<String> createQuery(List<String> queryWords) {
        ArrayList<String> query = new ArrayList<String>();
        
        for (int i = 0; i < queryWords.size(); i++) {
            String queryWord = queryWords.get(i);
            
            String[] caseFolding = caseFolding(queryWord);
            for (String word : caseFolding) {
                query.add(Utils.MOST_COMMON_ATTRIBUTES[i] + "." + word);
            }
        }        
        
        return query;
    }
    
    private static String[] caseFolding(String s) {
        s = s.toLowerCase();
        
        return s.split(" |\\+|\\@|\\#|\\=|\\%|\\)|\\(|\\:|\\;|\\,|<|>|\\}|\\{|\\/|[/]|\\*");
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
