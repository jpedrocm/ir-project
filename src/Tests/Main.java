package Tests;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;

import Crawl.Crawler;

public class Main {

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


    public static void main(String[] args) throws IOException {          
        Crawler crawler = new Crawler();

        for (int i = 0; i < URLS.length; i++) {
            String url = URLS[i];
            String domain = DOMAINS[i];
            
            HashSet<String> crawledURLs = crawler.crawlURL(url, domain, 1000, URL_FILTER);

//            String filename = url.split("\\.")[1] + "Filtered.txt";
//            System.out.println("Printing file " + filename);
//            saveURLs(filename, crawledURLs);

            crawledURLs = crawler.crawlURL(url, domain, 1000);

//            filename = url.split("\\.")[1] + ".txt";
//            System.out.println("Printing file " + filename);
//            saveURLs(filename, crawledURLs);
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

}