package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StaplesWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        Element productNameElement = doc.getElementsByAttributeValue("itemprop", "name").first();

        return productNameElement.text();
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        specifications.put("Name", Arrays.asList(getProductName(doc)));
        
        Elements specTables = doc.getElementsByClass("prod-specifications");
        
        for (Element specTable : specTables) {
            for (Element tableRow : specTable.getElementsByTag("tr")) {
                Elements rowContent = tableRow.getElementsByTag("td");
                String specName = rowContent.get(0).text();
                String specDescription = rowContent.get(1).text();
                
                specifications.put(specName, Arrays.asList(specDescription));
            }
        }        
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        return null;
    }

}
