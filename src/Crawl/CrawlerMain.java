package Crawl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;

import Utils.Utils;

public class CrawlerMain {    

    private static final int CRAWLER_MAX_ULRS = 1000;
    
    private static final String[] URL_FILTER = {"laptop", "notebook"};

    public static void main(String[] args) throws IOException { 
        for (int i = 0; i < Utils.DOMAIN.length; i++) {
            String initialURL = Utils.INITIAL_URLS[i];
            String domainURL = Utils.DOMAIN_URLS[i];
            String domain = Utils.DOMAIN[i];

            CrawlAndSave(initialURL, domainURL, domain, URL_FILTER);
            CrawlAndSave(initialURL, domainURL, domain);
        } 
    }
    
    private static void CrawlAndSave(String url, String domainURL, String domain, String... URL_Filter) throws FileNotFoundException, IOException {
        Crawler crawler = new Crawler(CRAWLER_MAX_ULRS);

        HashSet<String> crawledURLs = crawler.crawlURL(url, domainURL, URL_Filter);

        String filename = domain;

        if (URL_Filter.length > 0)
            filename += "Filtered";

        saveURLs(crawledURLs, filename);
        saveHTMLs(crawledURLs, filename);
    }

    private static void saveURLs(Iterable<String> urls, String filename) throws FileNotFoundException, IOException {
        System.out.println("Printing file " + filename);
        Utils.saveAllToFile(urls, Utils.CRAWLED_URLS_PATH, filename);        
    }

    private static void saveHTMLs(Iterable<String> urls, String directory) {
        int count = 0;

        for (String url : urls) {
            try {
                Utils.URLToFile(url, Paths.get(Utils.CRAWLED_HTML_PATH.toString(), directory), String.valueOf(count));                    
            } catch (Exception e) {
                e.printStackTrace();
            }

            count++;
        }
    }

}