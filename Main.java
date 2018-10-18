import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.Hashtable;
import java.awt.*;
import java.io.*;

public class Main {
    
    static private FileDialog openDia,saveDia;
    static private File file;
    // static private TextArea ta;

    public static void main(String[] args) {
        JFrame frame = new JFrame("JScratch");
        frame.setLayout(new BorderLayout());

        Model model = new Model();

        Canvas canvas= new Canvas(model);
        ToolView toolView = new ToolView(model);

        JMenu fileMenu = new JMenu("File");

        openDia = new FileDialog(frame,"Open",FileDialog.LOAD);
        saveDia = new FileDialog(frame,"Save",FileDialog.SAVE);

        for (String s: new String[] {"New File", "Load File", "Save File"}) {
            JMenuItem menuItem = new JMenuItem(s);
            menuItem.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JMenuItem mi = (JMenuItem)e.getSource();
                    if (mi.getText() == "Load File") {
                        doLoad();
                    } else if (mi.getText() == "Save File") {
                        doSave();
                    }
                }
            });
            fileMenu.add(menuItem);
        }

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        model.addObserver(canvas);
        model.addObserver(toolView);
        model.notifyObservers();

        frame.setJMenuBar(menuBar);
        frame.add(toolView, BorderLayout.LINE_START);
        frame.add(canvas);

        toolView.setPreferredSize(new Dimension(210,780));
        toolView.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        canvas. setPreferredSize(new Dimension(980,770));
        canvas.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // ta = new TextArea();
        // frame.add(ta);

        frame.setPreferredSize(new Dimension(1200,850));
        frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void doSave() {
        if (file == null) {
            saveDia.setVisible(true);
            String dirPath = saveDia.getDirectory();
            String fileName = saveDia.getFile();

            if(dirPath == null || fileName == null)
                return ;
            file = new File(dirPath,fileName);
        }
        
        try {
            BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
        
            // String text = ta.getText();

            // bufw.write(text);
        
            bufw.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("Save File Failed");
        }
    }

    private static void doLoad() {
        openDia.setVisible(true);
        String dirPath = openDia.getDirectory();
        String fileName = openDia.getFile();
    
        if(dirPath == null || fileName == null)
                return ;

        // ta.setText("");

        file = new File(dirPath,fileName);
        
        try
        {
                BufferedReader bufr = new BufferedReader(new FileReader(file));
                
                String line = null;

                while( (line = bufr.readLine())!= null)
                {
                    // ta.append(line +"\r\n");
                }
                bufr.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("Open File Failed");
        }
    }

}

