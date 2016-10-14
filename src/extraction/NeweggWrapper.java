package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NeweggWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        String name = null;
        
        Element productNameElement = doc.getElementById("grpDescrip_h");
        if (productNameElement != null)
            name = productNameElement.text();
        
        return name;
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = super.getSpecifications(doc);
        
        Elements specTitles = doc.getElementsByClass("specTitle");
        
        for (Element specTitle : specTitles) {
            Elements descriptionLists = specTitle.parent().getElementsByTag("dl");
            
            for (Element descriptionList : descriptionLists) {
                Element specElement = descriptionList.getElementsByTag("dt").first();
                Element specDescElement = descriptionList.getElementsByTag("dd").first();
                
                if (specElement != null && specDescElement != null)
                    specifications.put(specElement.text(), Arrays.asList(specDescElement.text()));
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
