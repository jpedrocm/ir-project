package Crawl;

import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

    public HashSet<String> crawlURL(String initialURL, String... filter) throws IOException {
        return crawlURL(initialURL, Integer.MAX_VALUE, filter);
    }
    
    public HashSet<String> crawlURL(String initialURL, int maxURLS, String... filter) {        
        URLQueue queue = new URLQueue(maxURLS);

        queue.offer(initialURL);
        
        while (queue.size() > 0 && queue.queuedURLSSize() < maxURLS) {
            String currentURL = queue.poll();
            Document doc = null;            
            
            try {
                doc = Jsoup.connect(currentURL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }    
            
            if (doc != null) {
                Elements allElements = doc.body().getElementsByAttribute("href");
                
                for (Element element : allElements) {
                    if (!element.tagName().toLowerCase().equals("script") && !element.tagName().toLowerCase().equals("link")) {
                        String url = element.attr("href");
                        
                        if (!url.isEmpty() 
                            && !url.startsWith("#") 
                            && (filter(url, filter) || filter(element.text(), filter))) {
                            
                            if (url.startsWith("//"))
                                url = "http:" + url;
                            else if (url.startsWith("/"))
                                if (initialURL.endsWith("/"))
                                    url = initialURL.substring(0, initialURL.length()-1) + url;
                                else
                                    url = initialURL + url; 
                            
                            if (url.startsWith(initialURL)) {
                                queue.offer(url);
                            }                            
                        }                    
                    }
                }
            }            
        }
        
        return queue.getQueuedURLS();
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
