package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JohnLewisWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        String name = null;
        
        Element prodNameElement = doc.getElementById("prod-title");
        if (prodNameElement != null)
            name = prodNameElement.text();        
        
        return name;
    }
    
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = super.getSpecifications(doc);
        
        Element prodInfo = doc.getElementById("prod-info-tab");
        if (prodInfo != null) {
            Elements descLists = prodInfo.getElementsByTag("dl");
            
            for (Element descList : descLists) {
                Element specName = descList.getElementsByTag("dt").first();
                Element specDesc = descList.getElementsByTag("dd").first();
                
                if (specName != null && specDesc != null)
                    specifications.put(specName.ownText(), Arrays.asList(specDesc.ownText()));
            }
        }
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        String price = null;
        
        Element priceElement = doc.getElementById("prod-price");
        if (priceElement != null)
            price = priceElement.text();        
        
        return price;
    }

}
