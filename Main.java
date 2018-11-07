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
    public static void main(String[] args) {
        JFrame frame = new JFrame("JScratch");
        frame.setLayout(new BorderLayout());

        Model model = new Model();

        Canvas canvas= new Canvas(model);
        ToolView toolView = new ToolView(model);
        
        openDia = new FileDialog(frame,"Open",FileDialog.LOAD);
        saveDia = new FileDialog(frame,"Save",FileDialog.SAVE);

        JMenu fileMenu = new JMenu("File");
        for (String s: new String[] {"New File", "Load File", "Save File"}) {
            JMenuItem menuItem = new JMenuItem(s);
            menuItem.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JMenuItem mi = (JMenuItem)e.getSource();
                    if (mi.getText() == "Load File") {
                        ArrayList<Shapes> sl= doLoad();
                        if (sl != null) {
                            model.notifyCanvasObservers();
                            canvas.setList(sl);
                            toolView.clean();
                        }
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

        JMenu viewMenu = new JMenu("View");
        for (String s: new String[] {"View Full Size", "View-Fit to Window"}) {
            JMenuItem menuItem = new JMenuItem(s);
            menuItem.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                }
            });
            viewMenu.add(menuItem);
        }

        menuBar.add(viewMenu);

        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0); 
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, "deselect");
        frame.getRootPane().getRootPane().getActionMap().put("deselect", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.selectedShape = null;
                model.toolButton = -1;
                model.clean();
            }
        });

        model.addCanvasObserver(canvas);
        model.addToolObserver(toolView);
        model.notifyCanvasObservers();

        frame.setJMenuBar(menuBar);
        frame.add(toolView, BorderLayout.WEST);
        frame.add(canvas, BorderLayout.EAST);

        toolView.setPreferredSize(new Dimension(210,800));
        toolView.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        
        JScrollPane scrollPane = new JScrollPane(canvas);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);

        canvas.setPreferredSize(new Dimension(1185,800));
        canvas.setBorder(BorderFactory.createLineBorder(Color.black, 2));

        frame.setMaximumSize(new Dimension(1400,850));
        frame.setPreferredSize(new Dimension(1400,850));
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
                    bufw.write(shape.fillColor.getRed());
                    bufw.write(shape.fillColor.getBlue());
                    bufw.write(shape.fillColor.getGreen());
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
                return null;

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
                    int fBlue = bufr.read();
                    int fGreen = bufr.read();
                    fillColor = new Color(retVal, fGreen, fBlue);
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

