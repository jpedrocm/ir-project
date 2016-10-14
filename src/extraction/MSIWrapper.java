package extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class MSIWrapper extends AbstractWrapper {

	@Override
	public String getProductName(Document doc) {
	    String name = null;
	    
		Element productNameElement = doc.getElementById("prod-title");
		if (productNameElement != null)
		    name = productNameElement.text();
		
		return name;
	}

	@Override
	public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = super.getSpecifications(doc);

		Elements specItems = doc.getElementsByClass("sku-spec-item");
		
		for(Element specItem : specItems){
		    Element specNameElement = specItem.getElementsByClass("spec-head").first();
		    
		    if (specNameElement != null) {
		        Element specBodyElement = specItem.getElementsByClass("spec-body").first();
		        
		        if (specBodyElement != null) {
		            String specName = specNameElement.text();
		            specifications.put(specName, new ArrayList<String>());
		            
		            List<Node> specValues = specBodyElement.childNodes();
		            
		            for (Node specValue : specValues)
		                if (specValue.nodeName().equals(("#text")))
		                    specifications.get(specName).add(specValue.toString().trim());
		        }
		    }
		}
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
		// MSI's website does not contain prices
		return null;
	}
}