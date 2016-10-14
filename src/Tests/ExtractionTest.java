package Tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import extraction.AbstractWrapper;
import extraction.LenovoWrapper;
import extraction.TigerDirectWrapper;
import extraction.ToshibaWrapper;

public class ExtractionTest {
    
    private static final String[] ToshibaURLs = {
    		"http://us.toshiba.com/computers/laptops/portege/A30/A30t-C1300ED",
			"http://us.toshiba.com/computers/laptops/portege/Z20t/Z20t-C2110",
			"http://us.toshiba.com/computers/laptops/portege/Z30/Z30-001011",
			"http://us.toshiba.com/computers/laptops/portege/A30/A30t-C1340",
			"http://us.toshiba.com/computers/laptops/tecra/Z50/Z50-034007",
			"http://us.toshiba.com/computers/laptops/tecra/C40/C40-0ND03K",
			"http://us.toshiba.com/computers/laptops/tecra/A40/A40-C1440",
			"http://us.toshiba.com/computers/laptops/tecra/C50/C50-C1502",
			"http://us.toshiba.com/computers/laptops/tecra/Z50/Z50-C1550",
			"http://us.toshiba.com/computers/laptops/tecra/A50/A50-03P01G"
    };
    
    private static final String[] TDURLs = {
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=8921430&CatId=4935",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=9943693&CatId=4935",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=4392337&CatId=3998",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=9960386&CatId=3998",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=3662718&CatId=4935",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=492238&CatId=4935",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=5480858&CatId=4935",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=9636943&CatId=4935",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=3240879&CatId=4935",
    		"http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=1785447&CatId=4935"
    };

    public static void main(String[] args) throws IOException {
    	
    	for(int i = 0; i < 10; i++){
	        AbstractWrapper wrapper = new TigerDirectWrapper();
	        Document doc =  wrapper.getDocument(TDURLs[i]);
	        
	        try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        
	        HashMap<String, List<String>> specs = wrapper.getSpecifications(doc);	        
		    
	        System.out.println(i+1);
	        for (String spec : specs.keySet())
	            for (String specDescription : specs.get(spec))
	                System.out.println(spec + ": " + specDescription);
	        System.out.println();
    	}
    }
}
