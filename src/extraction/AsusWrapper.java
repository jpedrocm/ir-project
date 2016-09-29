package extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class AsusWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        Element productNameElement = doc.getElementById("ctl00_ContentPlaceHolder1_ctl00_span_model_name");

        return productNameElement.text();
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        specifications.put("Name", Arrays.asList(getProductName(doc)));

        Element productSpecs = doc.getElementsByClass("product-spec").  first();
        Elements specList = productSpecs.getElementsByTag("li");

        for (Element listItem : specList) {            
            String specItem = listItem.getElementsByClass("spec-item").first().text();
            specifications.put(specItem, new ArrayList<String>());

            Element specData = listItem.getElementsByClass("spec-data").first(); 
            for (Node specDataItem : specData.childNodes()) {
                if (specDataItem.nodeName().equals("#text") && !specDataItem.toString().trim().isEmpty())
                    specifications.get(specItem).add(specDataItem.toString().trim());
            }
        }

        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        return null;
    }

}
