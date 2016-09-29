package Tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import extraction.AbstractWrapper;
import extraction.LenovoWrapper;

public class ExtractionTest {
    
    private static final String URL = "http://shop.lenovo.com/us/en/laptops/thinkpad/x-series/x1-carbon/";

    public static void main(String[] args) throws IOException {
        AbstractWrapper wrapper = new LenovoWrapper();
        Document doc =  wrapper.getDocument(URL);
        String productName = wrapper.getProductName(doc);
        HashMap<String, List<String>> specs = wrapper.getSpecifications(doc);
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
