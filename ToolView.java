import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;

public class ToolView extends JPanel implements Observer {

    private Model model;

    private JPanel buttonPanel = new JPanel();
    private JPanel colorPanel = new JPanel();
    private JPanel linePanel = new JPanel();
    
    private ArrayList<JButton> buttonList = new ArrayList<>();
    private ArrayList<JButton> colorList = new ArrayList<>();
    private ArrayList<JButton> lineList = new ArrayList<>();
    
    int buttonSize = 90;

    public ToolView(Model model) {
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(200,780));

        buttonPanel.setPreferredSize(new Dimension(200, 300));
        colorPanel.setPreferredSize(new Dimension(200, 350));
        linePanel.setPreferredSize(new Dimension(200, 135));
        linePanel.setLayout(new GridLayout(4, 1));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        colorPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        linePanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        this.add(buttonPanel);
        this.add(colorPanel);
        this.add(linePanel);

        // create toolkits
        for (String s: new String[] {"image/select.png", "image/eraser.png", "image/line.png",
         "image/circle.png", "image/square.png", "image/fill.png"}) {
            JButton button = new JButton(new ImageIcon(s));
            buttonList.add(button);
            button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            button.setPreferredSize(new Dimension(buttonSize,buttonSize));
            button.addActionListener(new toolButtonListener());
            buttonPanel.add(button);
        }

        // create colours pickers
        for (String s: new String[] {"image/red.png", "image/blue.png",
         "image/green.png", "image/orange.png",
         "image/yellow.png", "image/pink.png"}) {
            JButton button = new JButton(new ImageIcon(s));
            colorList.add(button);
            button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            button.addActionListener(new colorButtonListener());
            button.setPreferredSize(new Dimension(buttonSize, buttonSize));
            colorPanel.add(button);
        }

        JButton chooser = new JButton("Color Chooser");
        colorList.add(chooser);
        chooser.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        chooser.setPreferredSize(new Dimension(2*buttonSize, 50));
        chooser.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Color color = JColorChooser.showDialog(ToolView.this, "Choose a Color", Color.blue);
                chooser.setBorder(BorderFactory.createLineBorder(color, 8));
                model.chosenColor = color;
                for (JButton button: colorList) {
                   if (button != chooser) {
                        button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
                   }
                }
            }
        });
        colorPanel.add(chooser);

        int thickness = 2;
        for (String s: new String[] {"image/1.png", "image/2.png",
         "image/3.png", "image/4.png"}) {
            JButton button = new JButton(new ImageIcon(s));
            lineList.add(button);
            if (s == "image/1.png") {
                button.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            }
            button.addActionListener(new lineButtonListener());
            button.setPreferredSize(new Dimension(150, thickness));
            if (thickness == 2) {
                thickness = 9;
            } else if (thickness == 9) {
                thickness = 15;
            } else if (thickness == 15) {
                thickness = 20;
            }
            linePanel.add(button);
         }

        this.model = model;

        setVisible(true);
    }

    class toolButtonListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JButton button = (JButton)e.getSource();
            model.updateToolButton(button, buttonList);
        }
    }

    class colorButtonListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JButton button = (JButton)e.getSource();
            model.updateColorButton(button, colorList);
        }
    }

    class lineButtonListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JButton button = (JButton)e.getSource();
            model.updateLineButton(button, lineList);
        }
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        int colorPos = -1;
        if (model.chosenColor.equals(Color.decode("#FF0000"))) {
            colorPos = 0;
        } else if (model.chosenColor.equals(Color.decode("#115DA8"))) {
            colorPos = 1;
        } else if (model.chosenColor.equals(Color.decode("#5BC236"))) {
            colorPos = 2;
        } else if (model.chosenColor.equals(Color.decode("#FF6600"))) {
            colorPos = 3;
        } else if (model.chosenColor.equals(Color.decode("#FFFF00"))) {
            colorPos = 4;
        } else if (model.chosenColor.equals(Color.decode("#FF0080"))) {
            colorPos = 5;
        }
        for (int i = 0; i < colorList.size(); i ++) {
            if (i == colorList.size()-1) {
                colorList.get(i).setBorder(BorderFactory.createLineBorder(model.chosenColor, 8));
            } else if (i == colorPos) {
                colorList.get(i).setBorder(BorderFactory.createLineBorder(Color.black, 3));
            } else {
                colorList.get(i).setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            }
        }

        int width = model.lineWidth;
        int position;
        if (width == 2) {
            position = 0;
        } else if (width == 9) {
            position = 1;
        } else  if (width == 15) {
            position = 2;
        } else {
            position = 3;
        }
        for (int i = 0; i < lineList.size(); i ++) {
            if (i != position) {
                lineList.get(i).setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            } else {
                lineList.get(i).setBorder(BorderFactory.createLineBorder(Color.black, 3));
            }
        }
    }

    public void clear(Object observable) {
        for (JButton button: colorList) {
            if (button == colorList.get(colorList.size()-1)) {
                model.chosenColor = Color.black;
                button.setBorder(BorderFactory.createLineBorder(Color.black, 8));
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            }   
        }
        for (JButton button: lineList) {
            if (button == lineList.get(0)) {
                model.lineWidth = 2;
                button.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            }
        }
    }

    public void clean() {
        model.toolButton = -1;
        for (JButton button: buttonList) {
            button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
        }
        for (JButton button: colorList) {
            if (button == colorList.get(colorList.size()-1)) {
                button.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            }
        }
        for (JButton button: lineList) {
            if (button == lineList.get(0)) {
                button.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            } else {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            }
        }
    }
}
