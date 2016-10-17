package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {
    
    public static void URLToFile(String URL, Path directory, String filename) throws IOException {
        Document doc = Jsoup.connect(URL).get();        
        
        saveAllToFile(Arrays.asList(doc.html()), directory, filename);
    }

    public static Document fileToDoc(String filepath){
        Document doc = null;
        
        try {
            doc = Jsoup.parse(new File(filepath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return doc;
    }
    
    public static void createDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public static void saveAllToFile(Iterable<String> strings, Path directory, String filename) throws FileNotFoundException, IOException {
        String filepath = Paths.get(directory.toString(), filename).toString();
        
        createDirectory(directory);
        
        try (FileOutputStream fos = new FileOutputStream((filepath));
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                Writer writer = new BufferedWriter(osw)) {
            
            for (String string : strings)
                writer.write(string + "\n");
        }
    }
    
}
