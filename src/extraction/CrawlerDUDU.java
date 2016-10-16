package extraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerDUDU {
    static HashSet<String> urlsList;
    static LinkedList<Node> queue;
    public static void main(String[] args) throws IOException {

        ArrayList<HashSet<String>> urlContainer = new ArrayList<HashSet<String>>();

        long startTime = System.currentTimeMillis();


        //urlContainer.add(getCrawledURLs("http://store.hp.com/", "http://store.hp.com/", false));
        //urlContainer.add(getCrawledURLs("http://store.hp.com/", "http://store.hp.com/", true));
        //1 minuto, 39 sec

        //urlContainer.add(getCrawledURLs("http://us.toshiba.com", "http://us.toshiba.com", false));
        //urlContainer.add(getCrawledURLs("http://us.toshiba.com", "http://us.toshiba.com", true));
        //1 min, 37 sec

        //urlContainer.add(getCrawledURLs("http://shop.lenovo.com/us", "http://shop.lenovo.com/us", false));
        //urlContainer.add(getCrawledURLs("http://shop.lenovo.com/us", "http://shop.lenovo.com/", true));
        //3 min, 15 sec
        
        //urlContainer.add(getCrawledURLs("http://www.microcenter.com/", "http://www.microcenter.com", false));
        //urlContainer.add(getCrawledURLs("http://www.microcenter.com", "http://www.microcenter.com", true));
        //4 min, 6 sec

        //urlContainer.add(getCrawledURLs("http://www.newegg.com", "http://www.newegg.com", false));
        //urlContainer.add(getCrawledURLs("http://www.newegg.com", "http://www.newegg.com", true));
        //3 min, 3 sec
        
        
        //urlContainer.add(getCrawledURLs("http://www.asus.com/us/", "http://www.asus.com", false));
        urlContainer.add(getCrawledURLs("http://www.asus.com/us/", "http://www.asus.com", true));
        //5 min, 37 sec
        
        //urlContainer.add(getCrawledURLs("http://www.brandsmartusa.com/", "http://www.brandsmartusa.com/", false));
        //urlContainer.add(getCrawledURLs("http://www.brandsmartusa.com/", "http://www.brandsmartusa.com", true));
        //1 min, 24 sec

        //urlContainer.add(getCrawledURLs("http://www.staples.com/", "http://www.staples.com/", false));
        //urlContainer.add(getCrawledURLs("http://www.staples.com/", "http://www.staples.com/", true));
        //5 min, 9 sec
        
        
        //urlContainer.add(getCrawledURLs("http://www.bestbuy.com", "http://www.bestbuy.com", false));
        //urlContainer.add(getCrawledURLs("http://www.bestbuy.com/site/promo/blue-label-laptops-evn8139", "http://www.bestbuy.com", true));
        
        //urlContainer.add(getCrawledURLs("http://www.bestbuy.com", "http://www.bestbuy.com", true));
        //urlContainer.add(getCrawledURLs("http://www.computerlane.com", "http://www.computerlane.com", true));

        //urlContainer.add(getCrawledURLs("http://www.tigerdirect.com/applications/category/category_tlc.asp?CatId=17", "http://www.tigerdirect.com", true));
        
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Tempo de execução: "+totalTime);
        long a = 60000;
        String tempo = String.format("%d min, %d sec", 
                TimeUnit.MILLISECONDS.toMinutes(totalTime),
                TimeUnit.MILLISECONDS.toSeconds(totalTime) - 
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
                );
        System.out.println("Tempo de execução: "+tempo);
    }

    public static HashSet<String> getCrawledURLs(String url, String basicURL, boolean heuristic){

        Node start = new Node(0, url);

        queue = new LinkedList();
        urlsList = new HashSet();

        System.out.println("Adicionando no level "+start.nivel+" da fila: "+ start.url);
        queue.add(start);
        System.out.println("Tamanho da fila: "+queue.size());

        Node current = queue.getFirst();
        System.out.println("===================== STARTING BFS ====================");
        while(current.nivel <= 2 && queue.size() > 0 && urlsList.size() < 1000){
            System.out.println("Tamanho da fila: "+queue.size());
            current = queue.remove();
            System.out.println("Removendo no level "+current.nivel+" da fila: "+ current.url);
            try {

                enqueueUrlsOfNode(current, basicURL, heuristic);
                Thread.sleep(100);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                urlsList.remove(current.url);
            }
        }
        System.out.println("Número de urls crawleadas: "+urlsList.size());
        return urlsList;
    }

    public static LinkedList<Node> enqueueUrlsOfNode(Node node, String mainURL, boolean heuristica) throws IOException{
        String URL = node.url;
        int nivel = node.nivel + 1;

        Document doc = Jsoup.connect(URL).cookie("intl_splash", "false").get();
        Element body = doc.getElementsByTag("body").first();
        Elements nodes = body.getElementsByAttribute("href");
        //System.out.println(nodes.html());
        //System.out.println(nodes);
        for(Element e: nodes){
            //System.out.println(e.tagName());
            if(!e.tagName().equals("script") && !e.tagName().equals("link")){
                String url = e.attr("href");
                if(!url.isEmpty() && url.length() < 80){
                    //System.out.println(url);
                    //System.out.println("TAG "+e.tagName());
                    //System.out.println(e.text());


                    if(!heuristica || (heuristica && (url.toLowerCase().contains("laptop") 
                            || url.toLowerCase().contains("notebook") 
                            || e.text().toLowerCase().contains("laptop") 
                            || e.text().toLowerCase().contains("notebook") || (url.toLowerCase().contains("product") && URL.toLowerCase().contains("laptop"))))){

                        if(url.startsWith("//")){
                            url = "http:" + url;
                        }

                        if(url.startsWith("http") && url.contains(mainURL)){
                            Node n = new Node(nivel, url);
                            if(!urlsList.contains(n.url)){
                                System.out.println("if Adicionando no level "+n.nivel+" da fila "+queue.size()+": " + n.url);
                                queue.add(n);
                                urlsList.add(n.url);
                            }   
                        }else if (!url.startsWith("http")){
                            Node n = new Node(nivel, url);
                            n.url = mainURL + n.url;
                            if(!urlsList.contains(n.url)){
                                System.out.println("else Adicionando no level "+n.nivel+" da fila "+queue.size()+": " + n.url);
                                queue.add(n);
                                urlsList.add(n.url);
                            }   
                        }

                    }
                }
            }
        }


        return queue;
    }

}