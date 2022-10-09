/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package ObjetsGraph;

import java.awt.geom.Ellipse2D;

/**
 *
 * @author inky19
 */
public class Noeud extends Ellipse2D.Double {
    
    private double cx;
    private double cy;
    
    public Noeud(){
        super();
        cx = 0;
        cy = 0;
    }
    
    public Noeud(double cx, double cy, double width, double height){
        super(cx-width/2,cy-height/2,width,height);
        this.cx = cx;
        this.cy = cy;
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
}
