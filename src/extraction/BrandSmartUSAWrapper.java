package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BrandSmartUSAWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        Element title = doc.getElementsByTag("title").first();  
        
        return title.text();
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        specifications.put("Name", Arrays.asList(getProductName(doc)));
        
        Element specsTable = doc.getElementById("specs");
        Elements specsTableRows = specsTable.getElementsByTag("tr");
        
        for (Element row : specsTableRows) {
            String specItem = row.getElementsByTag("th").first().text();
            String specData = row.getElementsByTag("td").first().text();
            
            specifications.put(specItem, Arrays.asList(specData));            
        }
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        // TODO Auto-generated method stub
        return null;
    }

}
