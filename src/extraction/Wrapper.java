package extraction;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class Wrapper {
    
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public HashMap<String, List<String>> getSpecifications(Document doc) {
        return null;
    }
    
    public double getPrice(Document doc) {
        return 0;
    }
}
