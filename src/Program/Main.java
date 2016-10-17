package Program;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Crawl.Crawler;
import Utils.Utils;
import classification.Classificador;
import extraction.AbstractWrapper;
import extraction.GeneralExtractor;

public class Main {
    
    public static final boolean SAVE_TO_FILE = true;
    public static final String[] URL_FILTER = {"laptop", "notebook"};
    public static final String[] URLS = {            
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

    public static final String[] DOMAINS = { 
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

    public static void main(String[] args) {        
        Classificador filteredClassifier = new Classificador();
        Classificador unfilteredClassifier = new Classificador();        

        HashMap<String, ArrayList<String>> relevantsFiltered = filteredClassifier.classifyAllDocs(Paths.get("Data", "CrawledHTML").toString(), true, "Data/full_data.arff", "Data/random_forest.model");
        HashMap<String, ArrayList<String>> relevants = unfilteredClassifier.classifyAllDocs(Paths.get("Data", "CrawledHTML").toString(), false, "Data/full_data.arff", "Data/random_forest.model");

        HashMap<String, Double> filteredDomainHarvestRatio = filteredClassifier.calculateDomainsHarvestRatio();
        Double filteredTotalHarvestRatio = filteredClassifier.calculateTotalHarvestRatio();

        HashMap<String, Double> unfilteredDomainHarvestRatio = filteredClassifier.calculateDomainsHarvestRatio();
        Double unfilteredTotalHarvestRatio = filteredClassifier.calculateTotalHarvestRatio();

        System.out.println("CRAWLER RESULTS");

        for (String brand : BRANDS) {
            System.out.println("Domain: " + brand);
            System.out.println("Filtered domain harvest ratio: " + filteredDomainHarvestRatio);
            System.out.println("Unfiltered domain harvest ratio: " + unfilteredDomainHarvestRatio);
        }

        System.out.println("Filtered total harvest ratio: " + filteredTotalHarvestRatio);
        System.out.println("Unfiltered total harvest ratio: " + unfilteredTotalHarvestRatio);

        System.out.println();
        System.out.println("----------------------------------------------------------------------------");
        System.out.println();

        System.out.println("EXTRACTION RESULTS");
        for (int i = 0; i < BRANDS.length; i++) {
            String brand = BRANDS[i];

            System.out.println("Domain: " + brand);

            Path unfilteredHTMLPath = Paths.get("Data", "CrawledHTML", brand);
            Path filteredHTMLPath = Paths.get("Data", "CrawledHTML", brand + "Filtered");

            AbstractWrapper generalExtractor = new GeneralExtractor();

            long totalUnfilteredSpecificSpecifications = 0;
            long totalUnfilteredGeneralSpecifications = 0;
            long totalUnfilteredCorrectGeneralSpecifications = 0;
            for (String filename : relevants.get(unfilteredHTMLPath)) {
                String filePath = Paths.get(unfilteredHTMLPath.toString(), filename).toString();
                Document doc = Utils.fileToDoc(filePath);

                AbstractWrapper extractor = EXTRACTORS[i];
                HashMap<String, List<String>> specifications = extractor.getSpecifications(doc);
                HashMap<String, List<String>> generalSpecifications = generalExtractor.getSpecifications(doc);     

                for (String spec : specifications.keySet()) {
                    if (generalSpecifications.containsKey(spec) && specifications.get(spec).equals(generalSpecifications.get(spec)))
                        totalUnfilteredCorrectGeneralSpecifications++;
                }

                totalUnfilteredSpecificSpecifications += specifications.size();
                totalUnfilteredGeneralSpecifications += generalSpecifications.size();
            }

            long totalfilteredSpecificSpecifications = 0;
            long totalfilteredGeneralSpecifications = 0;
            long totalFilteredGeneralSpecifications = 0;
            for (String filename : relevantsFiltered.get(filteredHTMLPath)) {
                String filePath = Paths.get(filteredHTMLPath.toString(), filename).toString();
                Document doc = Utils.fileToDoc(filePath);

                AbstractWrapper extractor = EXTRACTORS[i];
                HashMap<String, List<String>> specifications = extractor.getSpecifications(doc);
                HashMap<String, List<String>> generalSpecifications = generalExtractor.getSpecifications(doc); 

                for (String spec : specifications.keySet()) {
                    if (generalSpecifications.containsKey(spec) && specifications.get(spec).equals(generalSpecifications.get(spec)))
                        totalFilteredGeneralSpecifications++;
                }

                totalfilteredSpecificSpecifications += specifications.size();
                totalfilteredGeneralSpecifications += generalSpecifications.size();
            }
        }
    }
    
    public static void saveURLs(String filename, HashSet<String> crawledURLs) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream((filename));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                Writer writer = new BufferedWriter(osw)) {

            for (String crawledURL : crawledURLs)
                writer.write(crawledURL + "\n"); 

        }
    }
    
    private static void saveToFile(String URL, String fileName, String domain) throws IOException {
        Path directory = Paths.get("Data", "CrawledHTML", domain);
        Path file = Paths.get(directory.toString(), fileName);
        
        try {
            Files.createDirectories(directory);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        Document doc = Jsoup.connect(URL).get();
        
        try (FileOutputStream fos = new FileOutputStream((file.toString()));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                Writer writer = new BufferedWriter(osw)) {
            
            writer.write(doc.html());            
        }
        
    }

}