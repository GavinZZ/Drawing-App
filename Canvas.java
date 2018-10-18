
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel implements Observer {

    private Model model;

    /*
     * Create a new View.
     */
    Canvas(Model model) {

        this.model = model;

        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        System.out.println("Model changed!");
    }
}
