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

/**
 *
 * @author inky19
 */
public class Noeud extends Ellipse2D.Double {
    
    private double cx;
    private double cy;
    public Color color;
    
    public Noeud(){
        super();
        this.
        cx = 0;
        cy = 0;
    }
    
    public Noeud(double cx, double cy, double width, double height){
        super(cx-width/2,cy-height/2,width,height);
        this.cx = cx;
        this.cy = cy;
        this.color = Color.WHITE;
    }
    
    public Noeud(double cx, double cy, double width, double height, Color color){
        super(cx-width/2,cy-height/2,width,height);
        this.cx = cx;
        this.cy = cy;
        this.color = color;
    }
    
    public void updateSize(double width, double height){
        super.height = height;
        super.width = width;
        super.x = cx-width/2;
        super.y = cy-height/2;
    }
    
    public void updatePos(double x, double y){
        cx = x;
        cy = y;
        super.x = cx-width/2;
        super.y = cy-height/2;
    }
    
    public void paint(Graphics2D g, boolean multiSelected, String label) {
        g.setStroke(new BasicStroke(7));
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
            Font font = new Font("Arial",Font.PLAIN,15);
            FontMetrics metrics = g.getFontMetrics(font);
            // Determine the X coordinate for the text
            int x = (int) (this.x + (this.width - metrics.stringWidth(label)) / 2);
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = (int) (this.y + ((this.height - metrics.getHeight()) / 2) + metrics.getAscent());
            // Set the font
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(label,x,y);
        }
    }
}
