package Tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Document;

import extraction.AbstractWrapper;
import extraction.GeneralExtractor;
import extraction.JohnLewisWrapper;

public class ExtractionTest {

    public static void main(String[] args) throws IOException {
        AbstractWrapper wrapper = new GeneralExtractor();
        
        for(int i = 0; i < 1; i++){
            //Document doc =  wrapper.getDocument("http://www.staples.com/Dell-Inspiron-I5555-0012-15-6-AMD-A8-Processor-6GB-RAM-500-GB-Hard-Drive-Windows-10-Black-Laptop/product_2276190##specificationsContent");
            //Document doc =  wrapper.getDocument("http://www.pcworld.co.uk/gbuk/computing/laptops/laptops/asus-x541ua-15-6-laptop-silver-10146347-pdt.html?intcmp=home~asus-laptop~product~pr-1~sixth~sale~pr~-~121016");
            //Document doc =  wrapper.getDocument("http://www.brandsmartusa.com/asus/187853/15+6+gaming+intel+core+laptop+computer.htm");
            //Document doc =  wrapper.getDocument("http://shop.lenovo.com/us/en/laptops/thinkpad/x-series/x1-carbon/");            
            //Document doc =  wrapper.getDocument("http://www.johnlewis.com/lenovo-ideapad-310-laptop-intel-core-i5-8gb-ram-1tb-15-6-/p2901246?navAction=jump");
            //Document doc =  wrapper.getDocument("http://us.toshiba.com/computers/laptops/tecra/C40/C40-C1400ED"); 
            //Document doc =  wrapper.getDocument("http://www.asus.com/us/Notebooks/ASUS-ZenBook-3-UX390UA/specifications/");
            
            //Document doc =  wrapper.getDocument("http://www.newegg.com/Product/Product.aspx?Item=N82E16834232776");

            //Document doc =  wrapper.getDocument("http://store.hp.com/us/en/pdp/hp---15z-laptop-%28touch-option-available%29-v1v04av-1");
            
            Document doc =  wrapper.getDocument("http://www.microcenter.com/product/461035/15-ac114nr_156_Laptop_Computer_-_Textured_Diamond_Pattern_in_Black");

            
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
