package extraction;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

public interface IWrapper {
    
    public Document getDocument(String url) throws IOException;

    public HashMap<String, List<String>> getSpecifications(Document doc);
    
    public double getPrice(Document doc);
}
