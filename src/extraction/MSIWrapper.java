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
		String productName = doc.getElementById("prod-title").text();
		return productName;
	}

	@Override
	public HashMap<String, List<String>> getSpecifications(Document doc) {
        HashMap<String, List<String>> specifications = new HashMap<String, List<String>>();

		specifications.put("Name", Arrays.asList(getProductName(doc)));

		Elements specItems = doc.getElementsByClass("sku-spec-item");
		
		for(Element specItem : specItems){
            String specName = specItem.getElementsByClass("spec-head").get(0).text();
			specifications.put(specName, new ArrayList<String>());
            List<Node> specValues = specItem.getElementsByClass("spec-body").get(0).childNodes();
            for(Node specValue : specValues)
            	if(specValue.nodeName().equals("#text"))
            		specifications.get(specName).add(specValue.toString().trim());
		}
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
		// MSI's website does not contain prices
		return null;
	}
}