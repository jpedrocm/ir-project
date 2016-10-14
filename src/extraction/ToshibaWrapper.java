package extraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ToshibaWrapper extends AbstractWrapper{

	@Override
	public String getProductName(Document doc) {
		String productName = doc.getElementsByClass("sku-title").get(0).text();
		return productName;
	}

	@Override
	public HashMap<String, List<String>> getSpecifications(Document doc) {
		HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();
		
		specifications.put("Name", Arrays.asList(getProductName(doc)));
        specifications.put("Price", Arrays.asList(getPrice(doc)));
		
		Elements specNames = doc.getElementsByTag("dt");
		Elements specValues = doc.getElementsByTag("dd");
		
		for(int i = 0; i < specNames.size(); i++){
			String specName = specNames.get(i).text();
			String specValue = specValues.get(i).text();
			specifications.put(specName, Arrays.asList(specValue));
		}
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
		String productPrice = doc.getElementById("sku-price").text();
		return productPrice;
	}
}