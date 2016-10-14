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
        String name = null;        
        
        Element productNameElement = doc.getElementsByAttributeValue("itemprop", "name").first();
        if (productNameElement != null)
            name = productNameElement.text();
        
        return name;
    }
    
    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) { 
        HashMap<String, List<String>> specifications = super.getSpecifications(doc); 

        Element specs = doc.getElementById("specs");        
        
        if (specs != null) {
            Elements rows = specs.getElementsByClass("row");  
            
            if (rows.size() > 2) {
                Elements configs = rows.get(1).getElementsByClass("large-12");

                for (Element config : configs) {
                    Element specElement = config.getElementsByTag("h2").first();
                    
                    if (specElement != null) {
                        String spec = specElement.text();
                        
                        specifications.put(spec, new ArrayList<String>());

                        for (Element specDescription : config.getElementsByTag("p")) {
                            specifications.get(spec).add(specDescription.text());
                        }
                    }                    
                }
            }            
        }        

        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        String price = null;
        
        Element priceElement = doc.getElementsByAttributeValue("itemprop", "price").first();  
        if (priceElement != null)
            price = priceElement.text();
        
        return price;
    }

}