package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

public abstract class AbstractWrapper {
    
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
    
    public abstract String getPrice(Document doc);
}