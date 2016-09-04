package extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class NeweggWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        Element productNameElement = doc.getElementById("grpDescrip_h");
        
        return productNameElement.text();
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        Elements specTitles = doc.getElementsByClass("specTitle");
        
        for (Element specTitle : specTitles) {
            Elements descriptionLists = specTitle.parent().getElementsByTag("dl");
            
            for (Element descriptionList : descriptionLists) {
                String spec = descriptionList.getElementsByTag("dt").first().text();
                String specDesc = descriptionList.getElementsByTag("dd").first().text();
                specifications.put(spec, new ArrayList<String>(Arrays.asList(specDesc)));
            }
        }        
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        Element priceElement = doc.getElementsByAttributeValue("itemprop", "price").first();
        
        return priceElement.attr("content");
    }

}
