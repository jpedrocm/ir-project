import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DellWrapper {

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://www.dell.com/us/p/xps-13-9350-laptop/pd?oc=dncwt5154b&model_id=xps-13-9350-laptop").get();
        
        System.out.println(doc);
    }
}
