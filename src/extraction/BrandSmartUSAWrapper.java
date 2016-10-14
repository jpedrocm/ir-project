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
        String name = null;       
        
        Element title = doc.getElementsByTag("title").first();  
        if (title != null)
            name = title.text();
        
        return name;
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        String name = getProductName(doc);
        if (name != null)
            specifications.put("Name", Arrays.asList(name));
        
        String price = getPrice(doc);
        if (price != null)
            specifications.put("Price", Arrays.asList(price));
        
        Element specsTable = doc.getElementById("specs");
        if (specsTable != null) {
            Elements specsTableRows = specsTable.getElementsByTag("tr");
            
            for (Element row : specsTableRows) {
                Element tableHeader = row.getElementsByTag("th").first();
                Element tableData = row.getElementsByTag("td").first();
                                
                if (tableHeader != null && tableData != null)
                    specifications.put(tableHeader.text(), Arrays.asList(tableData.text()));
                
            }
        }        
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        return null;
    }

}
