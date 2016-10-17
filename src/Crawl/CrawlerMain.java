package Crawl;

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
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Utils.Utils;
import classification.Classificador;
import extraction.AbstractWrapper;
import extraction.AsusWrapper;
import extraction.BrandSmartUSAWrapper;
import extraction.GeneralExtractor;
import extraction.HPWrapper;
import extraction.JohnLewisWrapper;
import extraction.LenovoWrapper;
import extraction.MicrocenterWrapper;
import extraction.NeweggWrapper;
import extraction.PCWorldWrapper;
import extraction.StaplesWrapper;
import extraction.ToshibaWrapper;

public class CrawlerMain {    

    public static final Path CRAWLED_HTML_PATH = Paths.get("Data", "CrawledHTML");
    public static final Path CRAWLED_URLS_PATH = Paths.get("Data", "CrawledURLS");

    public static final int CRAWLER_MAX_ULRS = 1000;

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

    public static final String[] BRANDS = {
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

    public static final AbstractWrapper[] EXTRACTORS = {
            new JohnLewisWrapper(),
            new NeweggWrapper(),
            new AsusWrapper(),
            new MicrocenterWrapper(),
            new ToshibaWrapper(),
            new LenovoWrapper(),
            new BrandSmartUSAWrapper(),
            new StaplesWrapper(),
            new PCWorldWrapper(),
            new HPWrapper()
    };

    public static void main(String[] args) throws IOException { 
        for (int i = 0; i < BRANDS.length; i++) {
            String url = URLS[i];
            String domain = DOMAINS[i];
            String brand = BRANDS[i];

            CrawlAndSave(url, domain, brand, URL_FILTER);
            CrawlAndSave(url, domain, brand);
        } 
    }
    
    public static void CrawlAndSave(String url, String domain, String brand, String... URL_Filter) throws FileNotFoundException, IOException {
        Crawler crawler = new Crawler(CRAWLER_MAX_ULRS);

        HashSet<String> crawledURLs = crawler.crawlURL(url, domain, URL_Filter);

        String filename = brand;

        if (URL_Filter.length > 0)
            filename += "Filtered";

        saveURLs(crawledURLs, filename);
        saveHTMLs(crawledURLs, filename);
    }

    public static void saveURLs(Iterable<String> urls, String filename) throws FileNotFoundException, IOException {
        System.out.println("Printing file " + filename);
        Utils.saveAllToFile(urls, CRAWLED_URLS_PATH, filename);        
    }

    public static void saveHTMLs(Iterable<String> urls, String directory) {
        int count = 0;

        for (String url : urls) {
            try {
                Utils.URLToFile(url, Paths.get(CRAWLED_HTML_PATH.toString(), directory), String.valueOf(count));                    
            } catch (Exception e) {
                e.printStackTrace();
            }

            count++;
        }
    }

}