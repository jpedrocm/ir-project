package extraction;

import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LenovoWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        Element productNameElement = doc.getElementsByAttributeValue("name", "ModelNumber").first();
        
        return productNameElement.attr("content");
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        specifications.put("Name", Arrays.asList(getProductName(doc)));

        Elements rows = doc.getElementsByClass("techSpecs-table");
        Element specs = rows.get(0);
        Elements trs = specs.getElementsByTag("tr");

        for(Element tr : trs){
            String descName = tr.getAllElements().get(0).text();
            String descDetail = tr.getAllElements().get(1).text();

            specifications.put(descName, Arrays.asList(descDetail));
        }
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        Element priceElement = doc.getElementsByAttributeValue("itemprop", "price").first();
        
        return priceElement.attr("content");
    }

}
