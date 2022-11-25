/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import Inforeg.UI.Vector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;

/**
 *
 * @author remir
 */
public class Line {
    public final static int DEFAULT_WIDTH = 8;
    public final static Color DEFAULT_COLOR = Color.BLUE;
    public Vector2D p1;
    public Vector2D p2;
    public int width;
    public Color color;
    public boolean arrow;
    
    public Line(Attachable from, Attachable to) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = DEFAULT_WIDTH;
        this.color = DEFAULT_COLOR;
        this.arrow = false;
    }
    
    public Line(Attachable from, Attachable to, Color color) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = DEFAULT_WIDTH;
        this.color = color;
        this.arrow = false;
    }
       
    public Line(Attachable from, Attachable to, int width) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = width;
        this.color = DEFAULT_COLOR;
        this.arrow = false;
    }
    
    public Line(Attachable from, Attachable to, int width, Color color) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = width;
        this.color = color;
        this.arrow = false;
    }
    
    public boolean contains(double x, double y) {
        Vector2D v = new Vector2D(p2.x-p1.x, p2.y-p1.y);
        double l = v.getNorm();
        Vector2D n = new Vector2D(-v.y, v.x);
        
        v.normalize();
        n.normalize();
        
        Vector2D p = new Vector2D(x-p1.x, y-p1.y);
        
        double dp1 = p.dotProduct(n);
        double dp2 = p.dotProduct(v);
        
        return (Math.abs(dp1) <= width/2 && Math.abs(dp2 - l/2) <= l/2);
        
    }
    
    public void paint(Draw d, Graphics2D g) {
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        paintLine(d,g,p1,p2);
        
        if (arrow) {
            paintArrow(d, g);
        }
    }
    
    public void paintLine(Draw d, Graphics2D g, Vector2D a, Vector2D b) {
        Vector2D v1 = d.toDrawCoordinates(a);
        Vector2D v2 = d.toDrawCoordinates(b);
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        g.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
    }
    
    
    public void paintArrow(Draw d, Graphics2D g) {
        Vector2D v = new Vector2D(p2.x-p1.x, p2.y-p1.y);
        Vector2D n = new Vector2D(-v.y, v.x);
        v.normalize();
        n.normalize();
        
        int h = (int)(10*Math.sqrt(width));
        int w = (int)(5*Math.sqrt(width));
        
        Vector2D middle = Vector2D.middle(p1, p2);
        
        Vector2D up = middle.plus(v.multiply(w/2));
        Vector2D a = middle.minus(v.multiply(w/2)).plus(n.multiply(h/2));
        Vector2D b = middle.minus(v.multiply(w/2)).minus(n.multiply(h/2));
        
        paintLine(d, g, up, a);
        paintLine(d, g, up, b);
    }
}
