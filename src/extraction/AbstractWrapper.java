package extraction;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class AbstractWrapper {
    
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
    
    public abstract String getProductName(Document doc);

    public abstract HashMap<String, List<String>> getSpecifications(Document doc);
    
    public abstract String getPrice(Document doc) ;
}