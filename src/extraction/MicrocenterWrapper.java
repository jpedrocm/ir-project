package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MicrocenterWrapper extends AbstractWrapper {

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

		Element specsTable = doc.getElementById("tab-specs");
		
		if (specsTable != null) {
		    Elements specBodies = specsTable.getElementsByClass("spec-body");
	        
	        for (Element specBody : specBodies) {
	            if (specBody.children().size() > 1) {
	                specifications.put(specBody.child(0).text(), Arrays.asList(specBody.child(1).text()));
	            }
	        }
		}		
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
		String price = null;
		
		Element priceElement = doc.getElementById("pricing");
		if (priceElement != null)
		    price = priceElement.text();
		
		return price;
	}
}