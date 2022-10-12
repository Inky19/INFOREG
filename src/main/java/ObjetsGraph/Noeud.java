/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package ObjetsGraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import utilities.Vector2D;
/**
 *
 * @author inky19
 */
public class Noeud extends Ellipse2D.Double {
    // Global coordinate of the node
    public double cx;
    public double cy;
    // Global radius
    public double r;
    // Color of the node
    public Color color;
    
    public Noeud(){
        super();
        this.
        cx = 0;
        cy = 0;
    }
    
    public Noeud(double cx, double cy, double r){
        this.cx = cx;
        this.cy = cy;
        this.r  = r;
        this.color = Color.WHITE;
    }
    
    public Noeud(double cx, double cy, double width, double height, Color color){
        super(cx-width/2,cy-height/2,width,height);
        this.cx = cx;
        this.cy = cy;
        this.color = color;
    }
    
    public void updateSize(double r){
        super.height = height;
        super.width = width;
        super.x = cx-width/2;
        super.y = cy-height/2;
        this.r = r;
    }
    
    public void updatePos(double x, double y){       
        cx = x;
        cy = y;
    }
    
    public void paint(Graphics2D g, boolean multiSelected, String label,float zoom,double cameraX, double cameraY) {
        this.x = zoom/100*(cx-r-cameraX) + cameraX;
        this.y = zoom/100*(cy-r-cameraY) + cameraY;
        this.height = 2*r*zoom/100;
        this.width = 2*r*zoom/100;
        
        g.setStroke(new BasicStroke(7*zoom/100));
        //Outline
        if(multiSelected){
            g.setPaint(Color.GREEN); 
        }else{
            g.setPaint(Color.BLACK);
        }
        
        g.draw(this);
        g.setStroke(new BasicStroke(1));
        //Inside
        g.setPaint(color);

        g.fill(this);
        //Label
        if (label != null) {
            Font font = new Font("Arial",Font.PLAIN,(int) (zoom/100*15));
            FontMetrics metrics = g.getFontMetrics(font);
            // Determine the X coordinate for the text
            int font_x = (int) (this.x + (this.width - metrics.stringWidth(label)) / 2);
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int font_y = (int) (this.y + ((this.height - metrics.getHeight()) / 2) + metrics.getAscent());
            // Set the font
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(label,font_x,font_y);
        }
    }
}
