package extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DellWrapper extends AbstractWrapper {

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
            
        Elements specTitles = doc.getElementsByClass("specTitle");
        Elements specContents = doc.getElementsByClass("specContent");
        
        Element specTitle;
        Element specContent;
        for (int i = 0; i < specTitles.size() && i < specContents.size(); i++) {
            specTitle = specTitles.get(i);
            specContent = specContents.get(i);
            
            Element specElement = specTitle.getElementsByTag("h5").first();
            Element contentElement = specContent.getElementsByClass("shortSpec").first();
            
            if (specElement != null && contentElement != null)
                specifications.put(specElement.text(), Arrays.asList(contentElement.text()));
        }
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        String price = null;
        
        Element dellPriceElement = doc.getElementsByClass("dellPrice").first();
        if (dellPriceElement != null) {
            Element priceElement = dellPriceElement.getElementsByClass("price").first();
            if (priceElement != null)
                price = priceElement.text();
        }
        
        return price;
    }

}
