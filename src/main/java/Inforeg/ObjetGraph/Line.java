/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import Inforeg.UI.Vector2D;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

/**
 *
 * @author remir
 */
public class Line {
    public final static int DEFAULT_WIDTH = 8;
    public Vector2D p1;
    public Vector2D p2;
    public int width;
    
    public Line(Attachable from, Attachable to) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = DEFAULT_WIDTH;
    }
    
    public Line(Attachable from, Attachable to, int width) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = width;
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
        Vector2D v1 = d.toDrawCoordinates(p1.x, p1.y);
        Vector2D v2 = d.toDrawCoordinates(p2.x, p2.y);
        
        g.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
    }
}
