package extraction;

import java.util.Arrays;
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
		
		specifications.put("Name", Arrays.asList(getProductName(doc)));
		specifications.put("Price", Arrays.asList(getPrice(doc)));
		
		Element specTable = doc.getElementsByClass("prodSpec").get(0);
		Elements specRows = specTable.getElementsByTag("tr");
		
		for(Element specRow : specRows){
			if(specRow.children().size() > 1){
				String specName = specRow.child(0).text();
				String specValue = specRow.child(1).text();
				specifications.put(specName, Arrays.asList(specValue));
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