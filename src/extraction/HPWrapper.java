package extraction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HPWrapper implements IWrapper {

    @Override
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
    
    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) { 
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

        Element specs = doc.getElementById("specs");        

        Elements rows = specs.getElementsByClass("row"); 

        Elements configs = rows.get(1).getElementsByClass("large-12");

        for (Element config : configs) {
            String spec = config.getElementsByTag("h2").first().text();
            specifications.put(spec, new ArrayList<String>());

            for (Element specDescription : config.getElementsByTag("p")) {
                specifications.get(spec).add(specDescription.text());
            }
        }

        return specifications;
    }

    @Override
    public double getPrice(Document doc) {
        return 0;
    }

}