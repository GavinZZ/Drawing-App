import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Shapes {
    int shape;
    // 1 for line, 2 for circle, 3 for rectangle
    Point startPos;
    Point endPos;
    Color shapeColor;
    Color fillColor;
    boolean fill;
    int lineWidth;
    Shapes(int s, int sx, int sy, int ex, int ey, Color c, int l) {
        shape = s;
        startPos = new Point(sx, sy);
        endPos = new Point(ex, ey);
        shapeColor = c;
        lineWidth = l;
        fill = false;
        fillColor = null;
    }    
}