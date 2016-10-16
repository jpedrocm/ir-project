package Crawl;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;

public class Crawler {
    
    public HashSet<String> crawlURL(String initialURL, String domain, String... filter) throws IOException {
        return crawlURL(initialURL, domain, Integer.MAX_VALUE, filter);
    }
    
    public HashSet<String> crawlURL(String initialURL, String domain, int maxURLS, String... filter) {          
        URLQueue queue = new URLQueue(maxURLS);
        BaseRobotRules robotRules = getRobotsRules(initialURL);

        queue.offer(initialURL);
        
        while (queue.size() > 0 && queue.queuedURLSSize() < maxURLS) {
            String currentURL = queue.poll();
            
            if (robotRules == null || robotRules.isAllowed(currentURL)) {
                Document doc = null;            
                
                int count = 0;
                
                try {
                    doc = Jsoup.connect(currentURL).get();
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }    
                
                if (doc != null) {
                    Elements allElements = doc.body().getElementsByAttribute("href");
                    
                    for (Element element : allElements) {
                        if (!element.tagName().toLowerCase().equals("script") && !element.tagName().toLowerCase().equals("link")) {
                            String url = element.attr("href");
                            
                            if (!url.isEmpty() 
                                && !url.startsWith("#") 
                                && !url.endsWith(".pdf")
                                && (filter(url, filter) || filter(element.text(), filter))) {
                                
                                if (url.startsWith("//"))
                                    url = "http:" + url;
                                else if (url.startsWith("/"))
                                    if (domain.endsWith("/"))
                                        url = domain.substring(0, domain.length()-1) + url;
                                    else
                                        url = domain + url; 
                                else if (!url.startsWith("http"))
                                    url = initialURL + url;
                                
                                if (url.startsWith(domain)) {
                                    queue.offer(url);
                                    count++;
                                }                            
                            }                    
                        }
                    }
                } 
                
                System.out.println(currentURL);
                System.out.println("Crawled pages: " + count);
                System.out.println();
            }            
        }
        
        return queue.getQueuedURLS();
    }
    
    private BaseRobotRules getRobotsRules(String url) {
        BaseRobotRules robotRules = null;
        SimpleRobotRulesParser rulesParser = new SimpleRobotRulesParser();
        
        if (url.endsWith("/"))
            url += "robots.txt";
        else
            url += "/robots.txt";
        
        String robots = null;
        
        try {
            robots = IOUtils.toString(new URI(url), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (robots != null) {
            robotRules = rulesParser.parseContent(url, robots.getBytes(), "text/plain", "Java version 1.8.0_101");            
        }
        
        return robotRules;
    }
    
    private boolean filter(String s, String... filter) {   
        if (filter == null || filter.length == 0)
            return true;
        
        for (String f : filter) {
            if (s.toLowerCase().contains(f.toLowerCase()))
                return true;
        }
        
        return false;
    }
    
    
}
