package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeneralExtractor extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

        Element tableElement = null;

        Elements allElements = new Elements();
        allElements.addAll(doc.getElementsByTag("table"));
        allElements.addAll(doc.getElementsByTag("div"));

        tableElement = searchSpecsTable(allElements);
        if (tableElement == null)
            tableElement = searchSpecsList(allElements);
        
        if (tableElement != null) {
            Elements rows = tableElement.getElementsByTag("tr");
            
            if (rows.size() == 0) 
                rows.addAll(tableElement.getElementsByTag("li"));
            
            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);

                if (row.children().size() == 2) {
                    specifications.put(row.child(0).text(), Arrays.asList(row.child(1).text()));
                }
            }
        }

        


        return specifications;
    }
    
    private Element searchSpecsList(Elements allElements) {
        Element listElement = null;
        
        
        
        return listElement;
    }
    
    private Element searchSpecsTable(Elements allElements) {
        Element tableElement = null;
        
        mainloop:
            for (Element element : allElements) {
                for (Attribute attribute : element.attributes()) {
                    if (attribute.getValue().toLowerCase().contains("spec")) {
                        if (element.tagName().toLowerCase().equals("table") || element.tagName().toLowerCase().equals("ul")) {
                            tableElement = element;
                            break mainloop;
                        }
                        else {
                            Elements tableChildren = element.getElementsByTag("table");
                            if (tableChildren.size() > 0) {
                                tableElement = tableChildren.first();
                                break mainloop;
                            }
                            
                            tableChildren = element.getElementsByTag("ul");
                            if (tableChildren.size() > 0) {
                                tableElement = tableChildren.first();
                                break mainloop;
                            }                            
                        }
                    }
                }
            }
        
        return tableElement;
    }

    @Override
    public String getPrice(Document doc) {
        // TODO Auto-generated method stub
        return null;
    }

}
