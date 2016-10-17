import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetHTMLSMain {

    public static void main(String[] args) {
        
        File crawledURLS = new File(Paths.get("Data", "CrawledURLS").toString());
        for (File file : crawledURLS.listFiles()) {
            try (InputStream in = Files.newInputStream(file.toPath());
                    BufferedReader reader =
                      new BufferedReader(new InputStreamReader(in))) {
                
                    System.out.println("Reading file: " + file.toPath().getFileName().toString());
                
                    String line = null;
                    int count = 0;
                    
                    while ((line = reader.readLine()) != null) {
                        Document doc = null;
                        
                        try {
                            System.out.println("Downloading url " + count + ": " + line);
                            doc = Jsoup.connect(line).get(); 
                            System.out.println("Downloaded");
                        }catch (Exception e) {
                            System.err.println(e);
                        }
                        
                        if (doc != null) {
                            FileWriter writer = new FileWriter(doc.html(), String.valueOf(count), file.toPath().getFileName().toString());
                            writer.start();
                            count++;
                        }
                    }
                    
                } catch (IOException x) {
                    System.err.println(x);
                }
        }

    }
}

class FileWriter extends Thread {
    
    private String html;
    private String filename;
    private String domain;
    
    public FileWriter(String html, String filename, String domain) {
        this.html = html;
        this.filename = filename;
        this.domain = domain;
    }
    
    @Override
    public void run() {
        Path directory = Paths.get("Data", "CrawledHTML", this.domain);
        Path file = Paths.get(directory.toString(), this.filename);
        
        try {
            Files.createDirectories(directory);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        try (FileOutputStream fos = new FileOutputStream((file.toString()));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                Writer writer = new BufferedWriter(osw)) {
            
            writer.write(this.html);            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
