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
	    String name = null;
	    
		Element productElement = doc.getElementsByClass("prodName").first();
		if (productElement != null) {
		    Element productNameElement = productElement.getElementsByTag("h1").first();
		    if (productNameElement != null)
		        name = productElement.text();
		}
		
		return name;
	}

	@Override
	public HashMap<String, List<String>> getSpecifications(Document doc) {
		HashMap<String, List<String>> specifications = super.getSpecifications(doc);
		
		Element specTable = doc.getElementsByClass("prodSpec").first();
		if (specTable != null) {
		    Elements specRows = specTable.getElementsByTag("tr");
	        
	        for(Element specRow : specRows){
	            if(specRow.children().size() > 1){
	                Element specNameElement = specRow.child(0);
	                Element specValueElement = specRow.child(1);
	                
	                if (specNameElement != null && specValueElement != null)
	                    specifications.put(specNameElement.text(), Arrays.asList(specValueElement.text()));
	            }
	        }
		}		
		
		return specifications;
	}

	@Override
	public String getPrice(Document doc) {
	    String price = null;	    
	    
	    Element priceElement = doc.getElementsByClass("salesPrice").first();
	    if (priceElement != null) {
	        price = priceElement.text().trim();
	        price = price.substring(0, price.length()-1);
	    }
	    
	    return price;
	}
}








