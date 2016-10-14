package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ToshibaWrapper extends AbstractWrapper{

	@Override
	public String getProductName(Document doc) {
	    String name = null;
	    
		Element productNameElement = doc.getElementsByClass("sku-title").first();
		if (productNameElement != null)
		    name = productNameElement.text();
		
		return name;
	}

	@Override
	public HashMap<String, List<String>> getSpecifications(Document doc) {
		HashMap<String, List<String>> specifications = super.getSpecifications(doc);
		
		Elements specNames = doc.getElementsByTag("dt");
		Elements specValues = doc.getElementsByTag("dd");
		
		for(int i = 0; i < specNames.size() && i < specValues.size(); i++) {
			String specName = specNames.get(i).text();
			String specValue = specValues.get(i).text();
			specifications.put(specName, Arrays.asList(specValue));
		}
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
	    String price = null;
	    
		Element productPriceElement = doc.getElementById("sku-price");
		if (productPriceElement != null)
		    price = productPriceElement.text();
		
		return price;
	}
}



