package extraction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HPWrapper extends AbstractWrapper {  

    @Override
    public String getProductName(Document doc) {
        Element productNameElement = doc.getElementsByAttributeValue("itemprop", "name").first();
        
        return productNameElement.text();
    }
    
    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) { 
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

        Element specs = doc.getElementById("specs");        

        Elements rows = specs.getElementsByClass("row"); 

        Elements configs = rows.get(1).getElementsByClass("large-12");

        for (Element config : configs) {
            String spec = config.getElementsByTag("h2").first().text();
            specifications.put(spec, new ArrayList<String>());

            for (Element specDescription : config.getElementsByTag("p")) {
                specifications.get(spec).add(specDescription.text());
            }
        }

        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        Element priceElement = doc.getElementsByAttributeValue("itemprop", "price").first();        
        
        return priceElement.text();
    }

}