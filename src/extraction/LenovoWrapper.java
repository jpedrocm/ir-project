package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LenovoWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        String name = null;
        
        Element productNameElement = doc.getElementsByAttributeValue("name", "ModelNumber").first();
        if (productNameElement != null) {
            if (productNameElement.hasAttr("content"))
                name = productNameElement.attr("content");
        }
        
        return name;
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = super.getSpecifications(doc);
        
        Element specTable = doc.getElementsByClass("techSpecs-table").first();
        if (specTable != null) {
            Elements tableRows = specTable.getElementsByTag("tr");

            for(Element tr : tableRows){  
                if (tr.children().size() == 2) {
                    String descName = tr.child(0).text();
                    String descDetail = tr.child(1).text();
                    
                    specifications.put(descName, Arrays.asList(descDetail));
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
            if (priceElement.hasAttr("content"))
                price = priceElement.attr("content");
        
        return price;
    }

}
