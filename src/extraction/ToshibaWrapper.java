package extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
		
		Element specTable = doc.getElementById("sku-specs");
		Elements specNames = doc.getElementsByTag("dt");
		Elements specValues = doc.getElementsByTag("dd");
		
		for(int i = 0; i < specNames.size(); i++){
			String specName = specNames.get(i).text();
			String specValue = specValues.get(i).text();
			List<String> valueList = new ArrayList<>();
			valueList.add(specValue);
			specifications.put(specName, valueList);
		}
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
		String productPrice = doc.getElementById("sku-price").text();
		return productPrice;
	}
}