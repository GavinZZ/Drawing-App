import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
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
                        canvas.setList(doLoad());
                        toolView.clean();
                    } else if (mi.getText() == "Save File") {
                        doSave(canvas.getShapeList());
                    } else if (mi.getText() == "New File") {
                        canvas.setList(createNew());
                        model.notifyCanvasObservers();
                        toolView.clean();
                        model.clean();
                    }
                }
            });
            fileMenu.add(menuItem);
        }

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        model.addCanvasObserver(canvas);
        model.addToolObserver(toolView);
        model.notifyCanvasObservers();

        frame.setJMenuBar(menuBar);
        frame.add(toolView, BorderLayout.LINE_START);
        frame.add(canvas, BorderLayout.CENTER);

        toolView.setPreferredSize(new Dimension(210,780));
        toolView.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        canvas.setPreferredSize(new Dimension(980,770));
        canvas.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        // ta = new TextArea();
        // frame.add(ta);

        frame.setPreferredSize(new Dimension(1200,850));
        frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static ArrayList<Shapes> createNew() {
        file = null;
        ArrayList<Shapes> sl = new ArrayList<>();
        return sl;
    }

    private static void doSave(ArrayList<Shapes> shapeList) {
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
            bufw.write(shapeList.size());;

            for (Shapes shape: shapeList) {
                bufw.write(shape.shape);
                bufw.write(shape.startPos.x);
                bufw.write(shape.startPos.y);
                bufw.write(shape.endPos.x);
                bufw.write(shape.endPos.y);
                bufw.write(shape.shapeColor.getRed());
                bufw.write(shape.shapeColor.getBlue());
                bufw.write(shape.shapeColor.getGreen());
                if (shape.fillColor == null) {
                    bufw.write(0);
                } else {
                    bufw.write(shape.fillColor.getRGB());
                }
                if (shape.fill) {
                    bufw.write(1);
                } else {
                    bufw.write(0);
                }
                bufw.write(shape.lineWidth);
            }
        
            bufw.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("Save File Failed");
        }
    }

    private static ArrayList<Shapes> doLoad() {
        openDia.setVisible(true);
        String dirPath = openDia.getDirectory();
        String fileName = openDia.getFile();
    
        if(dirPath == null || fileName == null)
                return new ArrayList<>();

        // ta.setText("");

        ArrayList<Shapes> sl = new ArrayList<>();

        file = new File(dirPath,fileName);
        
        try {
            BufferedReader bufr = new BufferedReader(new FileReader(file));
            
            int size = bufr.read();

            for (int i = 0; i < size; i ++) {
                int shape = bufr.read();
                int startPosX = bufr.read();
                int startPosY = bufr.read();
                int endPosX = bufr.read();
                int endPosY = bufr.read();
                int red = bufr.read();
                int blue = bufr.read();
                int green = bufr.read();
                Color shapeColor = new Color(red, green, blue);
                Color fillColor;
                int retVal = bufr.read();
                if (retVal != 0) {
                    fillColor = new Color(retVal);
                } else {
                    fillColor = null;
                }
                boolean fill;
                if (bufr.read() == 1) {
                    fill = true;
                } else {
                    fill = false;
                }
                int lineWidth = bufr.read();
                Shapes s = new Shapes(shape, startPosX, startPosY, endPosX, endPosY, shapeColor, lineWidth);
                s.fillColor = fillColor;
                s.fill = fill;
                sl.add(s);
            }

            bufr.close();
        } catch (IOException ex) {
            throw new RuntimeException("Open File Failed");
        }

        return sl;
    }

}

