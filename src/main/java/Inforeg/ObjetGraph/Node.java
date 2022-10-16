/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
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
 *
 * @author inky19
 */
public class Node extends Ellipse2D.Double {

    // Global coordinate of the node
    private double cx;
    private double cy;
    // Global radius
    private double r;
    // Color of the node
    private Color color;
    private String label;
    private int id;

    public Node() {
        super();
        this.cx = 0;
        cy = 0;
        label = "";
        this.id = 0;
    }

    public Node(double cx, double cy, double r, String label, int id) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = Color.WHITE;
        this.id = id;
        this.label = label;
    }

    public Node(double cx, double cy, double width, double height, Color color, String label, int id) {
        super(cx - width / 2, cy - height / 2, width, height);
        this.cx = cx;
        this.cy = cy;
        this.color = color;
        this.label = label;
        this.id = id;
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

    public void paint(Draw d, Graphics2D g, boolean selected) {
        // Update position and scale
        Vector2D v = d.toDrawCoordinates(cx - r, cy - r);
        this.x = v.x;
        this.y = v.y;
        double h = d.toDrawScale(2 * r);
        this.height = h;
        this.width = h;

        g.setStroke(new BasicStroke((float) d.toDrawScale(7)));
        //Outline
        if (selected) {
            g.setPaint(Color.GREEN);
        } else {
            g.setPaint(Color.BLACK);
        }
        g.draw(this);
        g.setStroke(new BasicStroke(1));
        //Inside
        g.setPaint(color);

        g.fill(this);
        //Label
        if (label != null) {
            Font font = new Font("Arial", Font.PLAIN, (int) d.toDrawScale(15));
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

    public Color getColor() {
        return color;
    }

    public String getColorHex() {
        String r = Integer.toHexString(color.getRed());
        String g = Integer.toHexString(color.getGreen());
        String b = Integer.toHexString(color.getBlue());
        return (r + g + b);
    }

    public void setColor(Color color) {
        this.color = color;
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

}
