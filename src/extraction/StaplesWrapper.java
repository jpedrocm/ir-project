package extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StaplesWrapper extends AbstractWrapper {

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
        
        Elements specTables = doc.getElementsByClass("prod-specifications");
        
        for (Element specTable : specTables) {
            for (Element tableRow : specTable.getElementsByTag("tr")) {
                Elements rowContent = tableRow.getElementsByTag("td");
                
                if (rowContent.size() >= 2) {
                    Element specNameElement = rowContent.get(0);
                    
                    List<String> specDescriptions = new ArrayList<String>();
                    for (int i = 1; i < rowContent.size(); i++) {
                        Element specDescriptionElement = rowContent.get(i);
                        if (specDescriptionElement != null)
                            specDescriptions.add(specDescriptionElement.text());
                    }
                    
                    specifications.put(specNameElement.text(), specDescriptions);
                    
                }
            }
        }        
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        return null;
    }

}
