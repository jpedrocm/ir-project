package analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.internal.win32.CREATESTRUCT;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import extraction.GeneralExtractor;
import utils.Utils;

public class Search {

    public static InvertedList createInvertedList(boolean isCompressed) throws FileNotFoundException, IOException {
        InvertedList invertedList;
        if (isCompressed)
            invertedList = new CompressedInvertedList();
        else
            invertedList = new DefaultInvertedList();
        
        InvertedList debug = new DefaultInvertedList();

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
                    int document = Integer.parseInt(file);
                    invertedList.addWord(word, document);
                }                            
            }
        }

        return invertedList;
    }

    public static LinkedHashMap<Integer, HashMap<String, List<String>>> search(InvertedList invertedList, Map<String, String> query, boolean isBoolean, int limit) throws FileNotFoundException, IOException {
        List<Pair> scores = calculateDocumentScores(invertedList, query, isBoolean, limit);

        LinkedHashMap<Integer, HashMap<String, List<String>>> result = new LinkedHashMap<>();

        GeneralExtractor extractor = new GeneralExtractor();                

        Set<String> mostCommonAttributes = Utils.getMostCommonAttributes();

        for (Pair pair : scores) {
            String documentPath = Utils.getDocumentPath(pair.key);

            Document doc = Jsoup.parse(Utils.readDocument(documentPath));

            HashMap<String, List<String>> specs = extractor.getSpecifications(doc);

            filterSpecs(specs, mostCommonAttributes); 
            
            result.put(pair.key, specs);
        }  

        return result;
    }

    private static List<Pair> calculateDocumentScores(InvertedList invertedList, Map<String, String> query, boolean isBoolean, int limit) {
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

            if (documents != null) {
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
        }       

        List<Pair> scoresList = new ArrayList<Pair>();
        for (Integer document : scores.keySet()) {
            double score = scores.get(document) / (Math.sqrt(normalizerDoc.get(document)) * Math.sqrt(normalizerQuery));
            if (Double.isFinite(score))
                scoresList.add(new Pair(document, score));
        }

        scoresList.sort((Pair p1, Pair p2) -> Double.compare(p2.value, p1.value));

        return scoresList.subList(0, Math.min(limit, scoresList.size()));
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

}
