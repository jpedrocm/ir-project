import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {

    static Document getHTML(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();
        return doc;
    }
}
