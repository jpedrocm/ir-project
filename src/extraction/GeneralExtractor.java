package extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeneralExtractor extends AbstractWrapper {

    private static final int MINIMUM_TABLE_SIZE = 10;

    @Override
    public String getProductName(Document doc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

        Elements tableElements = searchSpecsTable(doc.getElementsByTag("table"));

        if (tableElements.isEmpty())
            tableElements.addAll(searchSpecsDL(doc.getElementsByTag("dl")));

        if (!tableElements.isEmpty()) {
            for (Element tableElement : tableElements) {
                String tag = tableElement.tagName().toLowerCase();

                if (tag.equals("table")) {
                    specifications.putAll(getSpecsFromTable(tableElement));  
                }
                else if (tag.equals("dl"))
                    specifications.putAll(getSpecsFromDL(tableElement));                    
            }            
        }

        if (tableElements.isEmpty()) {
            specifications.putAll(getSpecsFromDIV(doc.getElementsByTag("div")));
        }

        return specifications;
    }

    private HashMap<String, List<String>> getSpecsFromDIV(Elements allElements) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

        for (Element element : allElements) {
            //search for a div that can be the start of a table, or contain a table
            if (element.id().contains("spec")) {      
                Elements allNodes = element.getAllElements();
                for (Element node : allNodes) {
                    //search for the processor description node
                    if (node.ownText().toLowerCase().startsWith("processor")) {
                        //Go back to the start of the table
                        Element table = node.parent();
                        while (table.children().size() < MINIMUM_TABLE_SIZE) {
                            table = table.parent();
                        }

                        for (Element row : table.children()) {
                            if (row.children().size() > 1) {
                                String specName = row.child(0).text();
                                specifications.put(specName, new ArrayList<String>());

                                for (int i = 1; i < row.children().size(); i++)
                                    specifications.get(specName).add(row.child(i).text());
                            }
                        }  
                    }
                }
            }
        }        

        return specifications;
    }

    private HashMap<String, List<String>> getSpecsFromDL(Element tableElement) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

        Elements dts = tableElement.getElementsByTag("dt");
        Elements dds = tableElement.getElementsByTag("dd");

        for (int i = 0; i < dts.size() && i < dds.size(); i++)
            specifications.put(dts.get(i).text(), Arrays.asList(dds.get(i).text()));           


        return specifications;
    }

    private HashMap<String, List<String>> getSpecsFromTable(Element tableElement) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

        Elements rows = tableElement.getElementsByTag("tr");

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);

            if (row.children().size() == 2) {
                specifications.put(row.child(0).text(), Arrays.asList(row.child(1).text()));
            }
        }

        return specifications;
    }

    private Elements searchSpecsDL(Elements descLists) {
        Elements specLists = new Elements();

        mainloop:
            for (Element list : descLists) {
                for (Element e : list.getAllElements()) {
                    if (e.text().toLowerCase().startsWith("processor")
                            || e.text().toLowerCase().startsWith("memory")
                            || e.text().toLowerCase().startsWith("ram")) {

                        Elements allDls = new Elements();
                        while (list.parent() != null && allDls.size() < MINIMUM_TABLE_SIZE) {
                            allDls.addAll(list.parent().getElementsByTag("dl"));      
                            for (Element sibling : list.parent().siblingElements())
                                allDls.addAll(sibling.getElementsByTag("dl")); 

                            list = list.parent();
                        }                        

                        specLists.addAll(allDls);
                        break mainloop;
                    }
                }  
            } 

        return specLists;
    }

    private Elements searchSpecsTable(Elements tableElements) {
        Elements specsTables = new Elements();

        mainloop:
            for (Element table : tableElements) {
                for (Element e : table.getAllElements()) {
                    if (e.text().toLowerCase().startsWith("processor")
                            || e.text().toLowerCase().startsWith("memory")
                            || e.text().toLowerCase().startsWith("ram")) {

                        specsTables.add(table); 
                        specsTables.addAll(table.siblingElements());
                        break mainloop;
                    }
                }  
            }        

        return specsTables;
    }

    @Override
    public String getPrice(Document doc) {
        // TODO Auto-generated method stub
        return null;
    }

}
