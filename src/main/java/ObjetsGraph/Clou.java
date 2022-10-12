/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ObjetsGraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

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
    
    public void paint(Graphics2D g, boolean multiSelected, float zoom,double cameraX, double cameraY) {
        this.x = zoom/100*(cx-r-cameraX) + cameraX;
        this.y = zoom/100*(cy-r-cameraY) + cameraY;
        this.height = 2*r*zoom/100;
        this.width = 2*r*zoom/100;
        
        g.setStroke(new BasicStroke(7*zoom/100));
        //Outline
        if(multiSelected){
            g.setPaint(Color.GREEN);
            g.draw(this);
        }
        
        g.setColor(color);
        g.fill(this);
    }
}
