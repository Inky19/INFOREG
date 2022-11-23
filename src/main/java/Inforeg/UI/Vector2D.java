/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.UI;

import java.awt.Point;

/**
 *
 * @author Rémi
 */
public class Vector2D {

    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public Point toPoint() {
        return new Point((int)x,(int)y);
    }
    
    public double dotProduct(Vector2D p) {
        return x*p.x + y*p.y;
    }
    
    public double getNorm() {
        return Math.sqrt(x*x+y*y);
    }
    
    public void normalize() {
        double l = getNorm();
        x /= l;
        y /= l;
    }

}
