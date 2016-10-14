package extraction;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class AbstractWrapper {
    
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
    
    public abstract String getProductName(Document doc);

    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        String name = getProductName(doc);
        if (name != null)
            specifications.put("Name", Arrays.asList(name));
        
        String price = getPrice(doc);
        if (price != null)
            specifications.put("Price", Arrays.asList(price));
        
        return specifications;
    }
    
    public abstract String getPrice(Document doc) ;
}