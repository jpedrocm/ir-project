package Tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import extraction.AbstractWrapper;
import extraction.BrandSmartUSAWrapper;
import extraction.DellWrapper;
import extraction.StaplesWrapper;

public class ExtractionTest {
    
    private static final String URL = "http://www.staples.com/Dell-i3552-3240BLK-15-6-Intel-Pentium-N3700-1-6GHz-Processor-4-GB-RAM-500-GB-HDD-Windows-10-Black-Notebook/product_2256788";

    public static void main(String[] args) throws IOException {
        AbstractWrapper wrapper = new StaplesWrapper();
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
