package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import Inforeg.UI.Vector2D;

/**
 * Nœud
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */

public class Node extends Ellipse2D.Double implements Attachable, Clickable, GraphObject {

    // Global coordinate of the node
    private double cx;
    private double cy;
    public Vector2D prevPos;
    // Global radius
    private double r;
    // Color of the node
    private Color color;
    private Color colorDisplayed;
    private Color outlineColor;
    private static int OUTLINE_WIDTH = 7;
    // Default color
    private final static Color MULTISELECTED_COLOR = Color.GREEN;
    private final static Color SELECTED_COLOR = Color.decode("#ddb9ff");
    private final static Color DEFAULT_COLOR = Color.WHITE;
    private final static Color DEFAULT_OUTLINE_COLOR = Color.BLACK;
    // Label of the node
    private String label;
    // id of the node
    private final int id;
    // Whether or not the node is selected
    private boolean multiSelected;
    private boolean selected;

    public Node() {
        super();
        multiSelected = false;
        cx = 0;
        cy = 0;
        label = "";
        this.id = 0;
    }

    public Node(double cx, double cy, double r, String label, int id) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = DEFAULT_COLOR;
        this.colorDisplayed = DEFAULT_COLOR;
        this.outlineColor = DEFAULT_OUTLINE_COLOR;
        this.id = id;
        this.label = label;
        multiSelected = false;
    }

    public Node(double cx, double cy, double r, Color color, String label, int id) {
        //super(cx-width/2,cy-height/2,width,height);
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = color;
        this.colorDisplayed = color;
        this.outlineColor = DEFAULT_OUTLINE_COLOR;
        this.label = label;
        this.id = id;
        multiSelected = false;
    }

    public void updateSize(double r) {
        super.height = height;
        super.width = width;
        super.x = cx - width / 2;
        super.y = cy - height / 2;
        this.r = r;
    }

    public void updatePos(double x, double y) {
        cx = x;
        cy = y;
    }
    
    @Override
    public void paint(Draw d, Graphics2D g) {
        // Update position and scale
        Vector2D v = d.toDrawCoordinates(cx - r, cy - r);
        this.x = v.x;
        this.y = v.y;
        double h = d.toDrawScale(2 * r);
        this.height = h;
        this.width = h;

        g.setStroke(new BasicStroke((float) d.toDrawScale(OUTLINE_WIDTH)));
        //Outline
        if (multiSelected) {
            g.setPaint(MULTISELECTED_COLOR);
        } else if (selected) {
            g.setPaint(SELECTED_COLOR);
        } else {
            g.setPaint(outlineColor);
        }
        g.draw(this);
        g.setStroke(new BasicStroke(1));
        //Inside
        g.setPaint(colorDisplayed);
        g.fill(this);
        //Label
        if (label != null) {
            Font font = new Font("Arial", Font.BOLD, (int) d.toDrawScale(15));
            FontMetrics metrics = g.getFontMetrics(font);
            // Determine the X coordinate for the text
            int font_x = (int) (this.x + (this.width - metrics.stringWidth(label)) / 2);
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int font_y = (int) (this.y + ((this.height - metrics.getHeight()) / 2) + metrics.getAscent());
            // Set the font
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(label, font_x, font_y);
        }
    }

    public double getCx() {
        return cx;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public void addCx(double dx) {
        this.cx += dx;
    }

    public double getCy() {
        return cy;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public void addCy(double dy) {
        this.cy += dy;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public Color getColorDisplayed() {
        return colorDisplayed;
    }

    public void setColorDisplayed(Color colorDisplayed) {
        this.colorDisplayed = colorDisplayed;
    }

    public void setColor(Color color) {
        this.color = new Color(color.getRGB());
        this.colorDisplayed = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return multiSelected || selected;
    }

    public void setMultiSelected(boolean selected) {
        this.multiSelected = selected;
    }

    public void setSelect(boolean selected) {
        this.selected = selected;
    }

    public void setOutlineColor(Color color) {
        this.outlineColor = color;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void reinit() {
        this.colorDisplayed = color;
        this.outlineColor = DEFAULT_OUTLINE_COLOR;
        this.selected = false;
        this.multiSelected = false;
    }

    @Override
    public String toString() {
        return "Noeud | label: " + label + ", x: " + cx + ", y: " + cy + " |";
    }

    @Override
    public Vector2D getCenterPos() {
        return new Vector2D(cx, cy);
    }

    @Override
    public double getRadius() {
        return r + OUTLINE_WIDTH / 2;
    }
}
