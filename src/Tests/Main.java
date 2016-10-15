package Tests;

import java.io.IOException;
import java.util.HashSet;

import Crawl.Crawler;

public class Main {
    
    public static final String[] URL_FILTER = {"laptop", "notebook" };

    public static void main(String[] args) throws IOException {  
        
        Crawler crawler = new Crawler();
        
        HashSet<String> crawledURLs = crawler.crawlURL("http://www.johnlewis.com/", 1000, URL_FILTER);
        
//        for (String s : crawledURLs)
//            System.out.println(s);
        
        System.out.println("Crawled urls:" + crawledURLs.size());
    }
    
//    public static HashSet<String> crawlURL(String URL) {
//        return crawlURL(URL, null);
//    }
    
    
    
    

}










