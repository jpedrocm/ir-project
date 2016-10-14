package extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class AsusWrapper extends AbstractWrapper {

    @Override
    public String getProductName(Document doc) {
        String name = null;

        Element productNameElement = doc.getElementById("ctl00_ContentPlaceHolder1_ctl00_span_model_name");

        if (productNameElement != null)
            name = productNameElement.text();

        return name;
    }

    @Override
    public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = super.getSpecifications(doc);

        Element productSpecs = doc.getElementsByClass("product-spec").first();
        if (productSpecs != null) {
            Elements specList = productSpecs.getElementsByTag("li");

            for (Element listItem : specList) {       
                Element specItem = listItem.getElementsByClass("spec-item").first();
                if (specItem != null) {
                    String specName = specItem.text();
                    specifications.put(specName, new ArrayList<String>());

                    Element specData = listItem.getElementsByClass("spec-data").first(); 
                    if (specData != null)
                        for (Node specDataItem : specData.childNodes()) {
                            if (specDataItem.nodeName().equals("#text") && !specDataItem.toString().trim().isEmpty())
                                specifications.get(specName).add(specDataItem.toString().trim());
                        }
                }

            }
        }


        return specifications;
    }

    @Override
    public String getPrice(Document doc) {
        return null;
    }

}
