/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inforeg.ObjetGraph;

import inforeg.Draw;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import utilities.Vector2D;

/**
 *
 * @author Rémi
 */
public class Clou extends Ellipse2D.Double {   
    public double cx;
    public double cy;
    public double r;
    public Color color;
    
    
    
    public Clou(double cx, double cy, double r) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = null;
    }
    
    public Clou(double cx, double cy, double r, Color color) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = color;
    }
    
    public void paint(Draw d, Graphics2D g, boolean selected) {
        Vector2D v = d.toDrawCoordinates(cx-r, cy-r);
        this.x = v.x;
        this.y = v.y;
        double h = d.toDrawScale(2*r);
        this.height = h;
        this.width = h;
        
        g.setStroke(new BasicStroke((float)d.toDrawScale(7)));
        //Outline
        if(selected){
            g.setPaint(Color.GREEN);
            g.draw(this);
        }
        
        g.setColor(color);
        g.fill(this);
    }

    public double getCx() {
        return cx;
    }

    public double getCy() {
        return cy;
    }
    
    
}
