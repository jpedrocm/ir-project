package Tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import extraction.HPWrapper;

public class ExtractionTest {
    
    private static final String URL = "http://store.hp.com/us/en/pdp/hp-envy---15t%C2%A0touch-laptop-w9c42av-1";

    public static void main(String[] args) throws IOException {
        HPWrapper wrapper = new HPWrapper();
        Document doc =  wrapper.getDocument(URL);
        HashMap<String, List<String>> specs = wrapper.getSpecifications(doc);
        String productName = wrapper.getProductName(doc);
        String price = wrapper.getPrice(doc);
        
        System.out.println(productName);
        System.out.println();
        
        System.out.println("Price: " + price);        
        System.out.println();
        
        for (String spec : specs.keySet())
            for (String specDescription : specs.get(spec))
                System.out.println(spec + ": " + specDescription);
    }

}
