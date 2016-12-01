package analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utils.Utils;

public class GUInterface {

    static boolean loading = true;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO Auto-generated method stub
        
        final Display display = new Display();
        Shell shell = new Shell(display);

        shell.setSize(500, 250);
        shell.setText("Recuperação de Informação - Laptops");

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;

        shell.setLayout(gridLayout);

        final Label nameLabel = new Label(shell, SWT.PUSH);
        nameLabel.setText("Name");
        nameLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Text nameInput = new Text(shell, SWT.SHADOW_IN);
        //nameInput.setText("dell");
        nameInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        final Label screenLabel = new Label(shell, SWT.PUSH);
        screenLabel.setText("Screen");
        screenLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Text screenInput = new Text(shell, SWT.SHADOW_IN);
        //screenInput.setText("14");
        screenInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        final Label cpuLabel = new Label(shell, SWT.PUSH);
        cpuLabel.setText("CPU");
        cpuLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Text cpuInput = new Text(shell, SWT.SHADOW_IN);
        //cpuInput.setText("intel");
        cpuInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        final Label portsLabel = new Label(shell, SWT.PUSH);
        portsLabel.setText("Ports");
        portsLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Text portsInput = new Text(shell, SWT.SHADOW_IN);
        //portsInput.setText("usb");
        portsInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        final Label storageLabel = new Label(shell, SWT.PUSH);
        storageLabel.setText("Storage");
        storageLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Text storageInput = new Text(shell, SWT.SHADOW_IN);
        //storageInput.setText("500gb");
        storageInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        final Label limitLabel = new Label(shell, SWT.PUSH);
        limitLabel.setText("Number of results");
        limitLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Combo c = new Combo(shell, SWT.READ_ONLY);
        c.setBounds(50, 50, 150, 65);
        String items[] = { "5", "10", "20" };
        c.setItems(items);
        
        final Label booleanLabel = new Label(shell, SWT.PUSH);
        booleanLabel.setText("Boolean search  ");
        booleanLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Button booleanInput = new Button(shell, SWT.CHECK);
        
        final Label compressedLabel = new Label(shell, SWT.PUSH);
        compressedLabel.setText("Compressed Inverted List  ");
        compressedLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        final Button compressedInput = new Button(shell, SWT.CHECK);
        
        final Button button = new Button(shell, SWT.PUSH);
        button.setText("Retrieve!");
        button.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        button.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent event) {
                Map<String, String> query = new HashMap<>();
                
                String cpu = cpuInput.getText().trim();
                if (!cpu.isEmpty())
                    query.put(Utils.ATTR_CPU, cpu);
                
                String name = nameInput.getText().trim();
                if (!name.isEmpty())
                    query.put(Utils.ATTR_NAME, name);
                
                String screen = screenInput.getText().trim();
                if (!screen.isEmpty())
                    query.put(Utils.ATTR_SCREEN, screen);
                
                String storage = storageInput.getText().trim();
                if (!storage.isEmpty())
                    query.put(Utils.ATTR_STORAGE, storage);                
                
                String ports = portsInput.getText().trim();
                if (!ports.isEmpty())
                    query.put(Utils.ATTR_PORTS, ports);
                
                int selected = c.getSelectionIndex();
                int limit = 0;
                if(selected == 0){
                    limit = 5;
                } else if (selected == 1){
                    limit = 10;
                } else {
                    limit = 20;
                }
                boolean isBoolean = booleanInput.getSelection();
                boolean isCompressed = compressedInput.getSelection();

                LinkedHashMap<Integer, HashMap<String, List<String>>> result = null;
                try {
                    InvertedList invertedList = Search.createInvertedList(isCompressed);
                    result = Search.search(invertedList, query, isBoolean, limit);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                StringBuilder sb = new StringBuilder();
                for (Integer document : result.keySet()) {
                    sb.append("Document " + document + "\n");
                    
                    HashMap<String, List<String>> specs = result.get(document);
                    for (String key : specs.keySet())
                        for (String value : specs.get(key))
                            sb.append(key + ": " + value + "\n");
                    
                    sb.append("###################################################################\n\n");
                    
                    //dell, 14, intel, usb, 500gb
                    //acer, 15, intel i7, thunderbolt, 1tb ssd
                    //hp, 13, i5, hdmi, 256gb
                    
                }
                
                final Shell shell2 = new Shell(display);
                shell2.setSize(540, 760);
                shell2.setText("Recuperação de Informação - Laptops - Resultados da pesquisa");
                
                Text labelWrap = new Text(shell2, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
                labelWrap.setBounds(10, 10, 500, 700);
                labelWrap.setText(sb.toString()); //resultado aqui
                
                shell2.open();
            }

            public void widgetDefaultSelected(SelectionEvent event) {

            }
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        //display.dispose();
    }
}