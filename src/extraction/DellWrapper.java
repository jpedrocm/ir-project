package extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DellWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        Element title = doc.getElementsByTag("title").first();
        
        return title.text();
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        Elements specTitles = doc.getElementsByClass("specTitle");
        Elements specContents = doc.getElementsByClass("specContent");
        
        Element specTitle;
        Element specContent;
        for (int i = 0; i < specTitles.size() && i < specContents.size(); i++) {
            specTitle = specTitles.get(i);
            specContent = specContents.get(i);
            
            String spec = specTitle.getElementsByTag("h5").first().text();
            String content = specContent.getElementsByClass("shortSpec").first().text();
            
            specifications.put(spec, new ArrayList<String>());          
            specifications.get(spec).add(content);
        }
        
        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        Element dellPrice = doc.getElementsByClass("dellPrice").first();
        Element price = dellPrice.getElementsByClass("price").first();
        
        return price.text();
    }

}
