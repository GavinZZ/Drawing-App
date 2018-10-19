import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class Canvas extends JPanel implements Observer {

    private Model model;

    Point startPos = new Point();
    Point endPos = new Point();

    Point M = new Point(); // mouse point
    Point C = new Point(); // click point

    int save = 0;
    boolean isDraging = false;

    private ArrayList<Shapes> shapeList = new ArrayList<>();

    Canvas(Model model) {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (model.toolButton == 3 || model.toolButton == 4 || model.toolButton == 5) {
                    if (save == 0) {
                        startPos.x = e.getX();
                        startPos.y = e.getY();
                    } else if (save == 1) {
                        endPos.x = e.getX();
                        endPos.y = e.getY();
                    }
                    save ++;
                    C.x = e.getX();
                    C.y = e.getY();
                } else if (model.toolButton == 1) {
                    isDraging = false;
                    startPos.x = e.getX();
                    startPos.y = e.getY();
                    save = 1;
                } else {
                    if (save == 0) {
                        startPos.x = e.getX();
                        startPos.y = e.getY();
                    }
                    save ++;
                }
                repaint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e){
                if (model.toolButton == 1) {
                    save = 1;
                    isDraging = true;
                    M.x = e.getX();
                    M.y = e.getY();
                    repaint();
                }
            }
        });
        
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e){
                if (model.toolButton == 1) {
                    endPos.x = e.getX();
                    endPos.y = e.getY();
                    isDraging = false;
                    save = 2;
                    repaint();
                }
            }
        });
 
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e){
                if (model.toolButton != 1) {
                    M.x = e.getX();
                    M.y = e.getY();
                    repaint();
                }
            }
        }); 

        this.model = model;

        setVisible(true);
    }

    double distance (Point a, Point b) {
        return Math.sqrt(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y,2));
    }

    boolean onTheLine(Shapes s, Point p) {
        if ((distance(s.startPos, p) + distance(s.endPos, p)) <= (distance(s.startPos, s.endPos) + 3.0) &&
        (distance(s.startPos, p) + distance(s.endPos, p)) >= (distance(s.startPos, s.endPos) - 3.0)) {
            return true;
        }
        return false;
    }

    boolean withInRange(Shapes s, Point p) {
        if (p.x >= s.startPos.x && p.y >= s.startPos.y && 
        p.x <= s.endPos.x && p.y <= s.endPos.y) {
            return true;
        }
        return false;
    }

    boolean withInCircle(Shapes s, Point p) {
        Ellipse2D ep = new Ellipse2D.Double(s.startPos.x, s.startPos.y, s.endPos.x-s.startPos.x, s.endPos.y-s.startPos.y);
        if (ep.contains(p.x, p.y)) {
            return true;
        }
        return false;
    }

    void selectShape(Graphics2D g2) {
        for (int j = shapeList.size()-1; j >= 0; j --) {
            Shapes s = shapeList.get(j);
            if (s.shape == 1) {
                if (onTheLine(s, startPos)) {
                    model.selectedShape(s.shapeColor, s.lineWidth);
                    model.selectedShape = s;
                    shapeList.remove(s);
                    shapeList.add(s);
                    if (isDraging) {
                        s.startPos = new Point(s.startPos.x+M.x-startPos.x, s.startPos.y+M.y-startPos.y);
                        s.endPos = new Point(s.endPos.x+M.x-startPos.x, s.endPos.y+M.y-startPos.y);
                        startPos.x = M.x;
                        startPos.y = M.y;
                    }  else if (save == 2) {
                        s.startPos = new Point(s.startPos.x+endPos.x-startPos.x, s.startPos.y+endPos.y-startPos.y);
                        s.endPos = new Point(s.endPos.x+endPos.x-startPos.x, s.endPos.y+endPos.y-startPos.y);
                        save = 0;
                    }
                    break;
                }
            } else if (s.shape == 2) {
                if (withInCircle(s, startPos)) {
                    model.selectedShape(s.shapeColor, s.lineWidth);
                    model.selectedShape = s;
                    shapeList.remove(s);
                    shapeList.add(s);
                    if (isDraging) {
                        s.startPos = new Point(s.startPos.x+M.x-startPos.x, s.startPos.y+M.y-startPos.y);
                        s.endPos = new Point(s.endPos.x+M.x-startPos.x, s.endPos.y+M.y-startPos.y);
                        startPos.x = M.x;
                        startPos.y = M.y;
                    }  else if (save == 2) {
                        s.startPos = new Point(s.startPos.x+endPos.x-startPos.x, s.startPos.y+endPos.y-startPos.y);
                        s.endPos = new Point(s.endPos.x+endPos.x-startPos.x, s.endPos.y+endPos.y-startPos.y);
                        save = 0;
                    }
                    break;
                }
            } else if (s.shape == 3) {
                if (withInRange(s, startPos)) {
                    model.selectedShape(s.shapeColor, s.lineWidth);
                    model.selectedShape = s;
                    shapeList.remove(s);
                    shapeList.add(s);
                    if (isDraging) {
                        s.startPos = new Point(s.startPos.x+M.x-startPos.x, s.startPos.y+M.y-startPos.y);
                        s.endPos = new Point(s.endPos.x+M.x-startPos.x, s.endPos.y+M.y-startPos.y);
                        startPos.x = M.x;
                        startPos.y = M.y;
                    }  else if (save == 2) {
                        s.startPos = new Point(s.startPos.x+endPos.x-startPos.x, s.startPos.y+endPos.y-startPos.y);
                        s.endPos = new Point(s.endPos.x+endPos.x-startPos.x, s.endPos.y+endPos.y-startPos.y);
                        save = 0;
                    }
                    break;
                }
            }
        }
    }

    void erase(Graphics2D g2) {
        for (int j = shapeList.size()-1; j >= 0; j --) {
            Shapes s = shapeList.get(j);
            if (s.shape == 1) {
                if (onTheLine(s, startPos)) {
                    shapeList.remove(j);
                    break;
                }
            } else if (s.shape == 2) {
                if (withInCircle(s, startPos)) {
                    shapeList.remove(j);
                    break;
                }
            } else if (s.shape == 3) {
                if (withInRange(s, startPos)) {
                    shapeList.remove(j);
                    break;
                }
            }
        }
        save = 0;
    }

    void drawLine(Graphics2D g2) {
        int width = model.lineWidth;
        Color color = model.chosenColor;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(width));
        g2.drawLine(C.x, C.y, M.x, M.y);
        if (save >= 2) {
            Shapes s = new Shapes(1, startPos.x, startPos.y, endPos.x, endPos.y, color, width);
            shapeList.add(s);
            save = 0;
        }
    }

    void drawCircle(Graphics2D g2) {
        int width = model.lineWidth;
        Color color = model.chosenColor; 
        g2.setColor(color);
        g2.setStroke(new BasicStroke(width));
        if (M.x >= C.x && M.y >= C.y) {
            g2.drawOval(C.x, C.y, M.x-C.x, M.y-C.y);
        } else if (M.x >= C.x && C.y >= M.y) {
            g2.drawOval(C.x, M.y, M.x-C.x, C.y-M.y);
        } else if (C.x >= M.x && M.y >= C.y) {
            g2.drawOval(M.x, C.y, C.x-M.x, M.y-C.y);
        } else {
            g2.drawOval(M.x, M.y, C.x-M.x, C.y-M.y);
        }
        if (save >= 2) {
            Shapes s;
            if (endPos.x >= startPos.x && endPos.y >= startPos.y) {
                s = new Shapes(2, startPos.x, startPos.y, endPos.x, endPos.y, color, width);
            } else if (M.x >= startPos.x && startPos.y >= endPos.y) {
                s = new Shapes(2, startPos.x, endPos.y, endPos.x, startPos.y, color, width);
            } else if (startPos.x >= endPos.x && startPos.y >= endPos.y) {
                s = new Shapes(2, endPos.x, endPos.y, startPos.x, startPos.y, color, width);
            } else {
                s = new Shapes(2, endPos.x, startPos.y, startPos.x, endPos.y, color, width);
            }
            shapeList.add(s);
            save = 0;
        }
    }

    void drawRectangle(Graphics2D g2) {
        int width = model.lineWidth;
        Color color = model.chosenColor;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(width));
        if (M.x >= C.x && M.y >= C.y) {
            g2.drawRect(C.x, C.y, M.x-C.x, M.y-C.y);
        } else if (M.x >= C.x && C.y >= M.y) {
            g2.drawRect(C.x, M.y, M.x-C.x, C.y-M.y);
        } else if (C.x >= M.x && M.y >= C.y) {
            g2.drawRect(M.x, C.y, C.x-M.x, M.y-C.y);
        } else {
            g2.drawRect(M.x, M.y, C.x-M.x, C.y-M.y);
        }
        if (save >= 2) {
            Shapes s;
            if (endPos.x >= startPos.x && endPos.y >= startPos.y) {
                s = new Shapes(3, startPos.x, startPos.y, endPos.x, endPos.y, color, width);
            } else if (M.x >= startPos.x && startPos.y >= endPos.y) {
                s = new Shapes(3, startPos.x, endPos.y, endPos.x, startPos.y, color, width);
            } else if (startPos.x >= endPos.x && startPos.y >= endPos.y) {
                s = new Shapes(3, endPos.x, endPos.y, startPos.x, startPos.y, color, width);
            } else {
                s = new Shapes(3, endPos.x, startPos.y, startPos.x, endPos.y, color, width);
            }
            shapeList.add(s);
            save = 0;
        }
    }

    void fillColor(Graphics2D g2) {
        for (int j = shapeList.size()-1; j >= 0; j --) {
            Shapes s = shapeList.get(j);
            if (s.shape == 1) {
                if (onTheLine(s, startPos)) {
                    if (s.fillColor != model.chosenColor) {
                        s.fill = true;
                        s.fillColor = model.chosenColor;
                    }
                    break;
                }
            } else if (s.shape == 2) {
                if (withInCircle(s, startPos)) {
                    if (s.fillColor != model.chosenColor) {
                        s.fill = true;
                        s.fillColor = model.chosenColor;
                    }
                    break;
                }
            } else if (s.shape == 3) {
                if (withInRange(s, startPos)) {
                    if (s.fillColor != model.chosenColor) {
                        s.fill = true;
                        s.fillColor = model.chosenColor;
                    }
                    break;
                }
            }
        }
        save = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; 
        int tool = model.toolButton;
        if (tool == 1 && save > 0) {
            selectShape(g2);
        } else if (tool == 2 && save > 0) {
            erase(g2);
        } else if (tool == 3 && save > 0) {
            drawLine(g2);
        } else if (tool == 4 && save > 0) {
            drawCircle(g2);
        } else if (tool == 5 && save > 0) {
            drawRectangle(g2);
        } else if (tool == 6 && save > 0) {
            fillColor(g2);
        }

        for (Shapes s: shapeList) {
            g2.setColor(s.shapeColor);
            g2.setStroke(new BasicStroke(s.lineWidth));
            if (s.shape == 1) {
                g2.drawLine(s.startPos.x, s.startPos.y, s.endPos.x, s.endPos.y);
            } else if (s.shape == 2) {
                Ellipse2D ep = new Ellipse2D.Double(s.startPos.x, s.startPos.y,
                    s.endPos.x-s.startPos.x, s.endPos.y-s.startPos.y);
                g2.draw(ep);
                if (s.fillColor != null) {
                    g2.setPaint(s.fillColor);
                    g2.fill(ep);
                }
            } else if (s.shape == 3) {
                Rectangle2D rt = new Rectangle2D.Double(s.startPos.x, s.startPos.y, s.endPos.x-s.startPos.x, s.endPos.y-s.startPos.y);
                g2.draw(rt);
                if (s.fillColor != null) {
                    g2.setPaint(s.fillColor);
                    g2.fill(rt);
                }
            }
        }
    }

    void setList(ArrayList<Shapes> sl) {
        shapeList = sl;
    }

    ArrayList<Shapes> getShapeList() {
        return shapeList;
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        repaint();
        save = 0;
        startPos = new Point();
        endPos = new Point();
    }

    public void clear(Object observable) { }
}

