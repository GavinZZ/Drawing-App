
import java.util.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.JButton;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList<Observer>();
    }

    int toolButton;
    Color chosenColor;
    int lineWidth;

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }
    
    //  green : 5BC236
    //  blue : 115DA8
    //  yellow : ffff00
    //  orange : ff6600
    //  red : ff0000
    //  pink : ff0080

    private int findTool(JButton selectedTool, ArrayList<JButton> buttonList) {
        if (selectedTool.getIcon().toString() == "image/select.png") {
            return 1;
        } else if (selectedTool.getIcon().toString() == "image/eraser.png") {
            return 2;
        } else if (selectedTool.getIcon().toString() == "image/line.png") {
            return 3;
        } else if (selectedTool.getIcon().toString() == "image/circle.png") {
            return 4;
        } else if (selectedTool.getIcon().toString() == "image/square.png") {
            return 5;
        } else {
            return 6;
        }
    }

    private Color findColor(JButton selectedColor, ArrayList<JButton> buttonList) {
        if (selectedColor.getIcon().toString() == "image/red.png") {
            return Color.decode("#FF0000");
        } else if (selectedColor.getIcon().toString() == "image/blue.png") {
            return Color.decode("#115DA8");
        } else if (selectedColor.getIcon().toString() == "image/green.png") {
            return Color.decode("#5BC236");
        } else if (selectedColor.getIcon().toString() == "image/orange.png") {
            return Color.decode("#FF6600");
        } else if (selectedColor.getIcon().toString() == "image/yellow.png") {
            return Color.decode("#FFFF00");
        } else {
            return Color.decode("#FF0080");
        }
    }

    private int findWdith(JButton selectedLine, ArrayList<JButton> buttonList) {
        if (selectedLine.getIcon().toString() == "image/1.png") {
            return 1;
        } else if (selectedLine.getIcon().toString() == "image/2.png") {
            return 2;
        } else if (selectedLine.getIcon().toString() == "image/3.png") {
            return 3;
        } else {
            return 4;
        }
    }

    public void updateToolButton(JButton selectedTool, ArrayList<JButton> buttonList) {
        for (JButton button: buttonList) {
            if (selectedTool != button) {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            } else {
                toolButton = findTool(selectedTool, buttonList);
                button.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            }
        }
    }

    public void updateColorButton(JButton selectedColor, ArrayList<JButton> buttonList) {
        JButton lastItem = buttonList.get(buttonList.size()-1);
        Color bColor = findColor(selectedColor, buttonList);
        for (JButton button: buttonList) {
            if (selectedColor != button && button != lastItem) {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            } else if (selectedColor == button && button != lastItem) {
                chosenColor = bColor;
                button.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            } else if (button == lastItem) {
                button.setBorder(BorderFactory.createLineBorder(bColor, 5));
            }
        }
    }

    public void updateLineButton(JButton selectedLine, ArrayList<JButton> buttonList) {
        int width = findWdith(selectedLine, buttonList);
        for (JButton button: buttonList) {
            if (selectedLine != button) {
                button.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
            } else {
                lineWidth = width;
                button.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            }
        }
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }
}
