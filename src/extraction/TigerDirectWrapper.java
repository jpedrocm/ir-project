package extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TigerDirectWrapper extends AbstractWrapper {

	@Override
	public String getProductName(Document doc) {
		Element product = doc.getElementsByClass("prodName").get(0);
		String productName = product.getElementsByTag("h1").get(0).text();
		return productName;
	}

	@Override
	public HashMap<String, List<String>> getSpecifications(Document doc) {
		HashMap<String, List<String>> specifications = new HashMap<>();
		
		List<String> listOfNames = new ArrayList<String>();
		listOfNames.add(getProductName(doc));
		specifications.put("model", listOfNames);
		List<String> listOfPrices = new ArrayList<String>();
		listOfPrices.add(getPrice(doc));
		specifications.put("price", listOfPrices);
		
		Element specTable = doc.getElementsByClass("prodSpec").get(0);
		Elements specRows = specTable.getElementsByTag("tr");
		
		for(Element specRow : specRows){
			if(specRow.children().size() > 1){
				String specName = specRow.child(0).text();
				String specValue = specRow.child(1).text();
				List<String> valueList = new ArrayList<String>();
				valueList.add(specValue);
				specifications.put(specName, valueList);
			}
		}
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
		String price = doc.getElementsByClass("salePrice").get(0).text().trim();
        StringBuilder priceBuilder = new StringBuilder(price);
        priceBuilder.setCharAt(price.length()-1,' ');
        String productPrice = priceBuilder.toString().trim();
		return productPrice;
	}
}