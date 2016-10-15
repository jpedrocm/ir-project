package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PCWorldWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        String name = null;
        
        Element productNameElement = doc.getElementsByAttributeValueMatching("property", "og:title").first();
        
        if (productNameElement != null) {
            name = productNameElement.attr("content");
        }        
        
        return name;
    }

    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = super.getSpecifications(doc);

        Element tabTechSpecs = doc.getElementsByClass("tab-tech-specs").first();
        
        if (tabTechSpecs != null) {
            Elements techTables = tabTechSpecs.getElementsByTag("table");
            
            for (Element table : techTables) {
                Elements tableHeaders = table.getElementsByTag("th");
                Elements tableData = table.getElementsByTag("td");
                
                for (int i = 0; i < tableHeaders.size() && i < tableData.size(); i++) {
                    Element header = tableHeaders.get(i);
                    Element data = tableData.get(i);
                    
                    specifications.put(header.text(), Arrays.asList(data.text()));
                }
            }
        }

        return specifications;
    }

    @Override
    public String getPrice(Document doc) {       
        String price = null;
        
        Element productActions = doc.getElementById("product-actions");
        
        if (productActions != null) {
            Element priceElement = productActions.getElementsByAttributeValue("data-key", "current-price").first();
            
            if (priceElement != null)
                price = priceElement.text();
        }        
        
        return price;
    }

}
