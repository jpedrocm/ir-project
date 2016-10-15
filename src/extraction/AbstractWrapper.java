package extraction;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class AbstractWrapper {
    
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
    
    public abstract String getProductName(Document doc);

    public HashMap<String, List<String>> getSpecifications(Document doc) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("filename.txt"), "utf-8"))) {
     writer.write(doc.html());
  } catch (UnsupportedEncodingException e) {
    e.printStackTrace();
} catch (FileNotFoundException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
        
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
        
        String name = getProductName(doc);
        if (name != null)
            specifications.put("Name", Arrays.asList(name));
        
        String price = getPrice(doc);
        if (price != null)
            specifications.put("Price", Arrays.asList(price));
        
        return specifications;
    }
    
    public abstract String getPrice(Document doc) ;
}