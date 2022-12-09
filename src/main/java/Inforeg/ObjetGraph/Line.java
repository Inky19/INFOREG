/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import Inforeg.UI.Vector2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author remir
 */
public class Line {

    public final static int DEFAULT_WIDTH = 8;
    public final static Color DEFAULT_COLOR = Color.BLUE;
    public final static int CIRCLE_RADIUS = 15;
    public Vector2D p1;
    public Vector2D p2;
    public int width;
    public Color color;
    public boolean arrow = false;
    public boolean circle = false;

    public Line(Attachable from, Attachable to) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = DEFAULT_WIDTH;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, Attachable to, Color color) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = DEFAULT_WIDTH;
        this.color = color;
    }

    public Line(Attachable from, Attachable to, int width) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = width;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, Attachable to, int width, Color color) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = width;
        this.color = color;
    }

    public Line(Attachable from) {
        this.p1 = from.getCenterPos();
        this.p2 = null;
        this.width = DEFAULT_WIDTH;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, int width) {
        this.p1 = from.getCenterPos();
        this.p2 = null;
        this.width = width;
        this.color = DEFAULT_COLOR;
    }

    public Line(Attachable from, int width, Color color) {
        this.p1 = from.getCenterPos();
        this.p2 = null;
        this.width = width;
        this.color = color;
    }
    
    public Line(Attachable from, Attachable to, int width, Color color, boolean circle) {
        this.p1 = from.getCenterPos();
        this.p2 = to.getCenterPos();
        this.width = width;
        this.color = color;
        this.circle = circle;
    }
    
    public boolean contains(double x, double y) {
        if (p1 == null && p2 == null) {
            return false;
        } else if (p1 == null || p2 == null) {
            Vector2D v = new Vector2D(x, y);
            double dist;
            if (p1 == null) {
                dist = v.minus(p2.plus(new Vector2D(CIRCLE_RADIUS, 0))).getNorm();
            } else {
                dist = v.minus(p1.plus(new Vector2D(CIRCLE_RADIUS, 0))).getNorm();
            }
            return (CIRCLE_RADIUS - width / 2 <= dist && dist <= CIRCLE_RADIUS + width / 2);
        } else {
            Vector2D v = new Vector2D(p2.x - p1.x, p2.y - p1.y);
            double l = v.getNorm();
            Vector2D n = new Vector2D(-v.y, v.x);

            v.normalize();
            n.normalize();

            Vector2D p = new Vector2D(x - p1.x, y - p1.y);

            double dp1 = p.dotProduct(n);
            double dp2 = p.dotProduct(v);

            return (Math.abs(dp1) <= width / 2 && Math.abs(dp2 - l / 2) <= l / 2);
        }
    }

    public void paint(Draw d, Graphics2D g) {
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        if (p1 == null && p2 == null) {
            // nothing to paint
        } else if (p1 == null || p2 == null) {
            paintCircle(d, g, p2==null ? p1 : p2);
        } else {
            if (circle) {
                paintCircle(d, g, p1, p2);
            } else {
                paintLine(d, g, p1, p2);
                if (arrow) {
                    paintArrow(d, g);
                }
            }
        }
    }

    private void paintLine(Draw d, Graphics2D g, Vector2D a, Vector2D b) {
        Vector2D v1 = d.toDrawCoordinates(a);
        Vector2D v2 = d.toDrawCoordinates(b);
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
    }

    private void paintCircle(Draw d, Graphics2D g, Vector2D a) {
        Vector2D aPos = d.toDrawCoordinates(a);
        Vector2D bPos = d.toDrawCoordinates(a.plus(new Vector2D(2 * CIRCLE_RADIUS, 0)));
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        double radius = d.toDrawScale(CIRCLE_RADIUS);
        g.draw(new Ellipse2D.Double((aPos.x + bPos.x) / 2 - radius, (aPos.y + bPos.y) / 2 - radius, 2 * radius, 2 * radius));
    }
    private void paintCircle(Draw d, Graphics2D g, Vector2D a, Vector2D b) {
        Vector2D aPos = d.toDrawCoordinates(a);
        Vector2D bPos = d.toDrawCoordinates(b);
        g.setStroke(new BasicStroke((float) d.toDrawScale(width)));
        g.setColor(color);
        double radius = d.toDrawScale(Vector2D.dist(a, b)/2);
        g.draw(new Ellipse2D.Double((aPos.x + bPos.x) / 2 - radius, (aPos.y + bPos.y) / 2 - radius, 2 * radius, 2 * radius));
    }

    private void paintArrow(Draw d, Graphics2D g) {
        Vector2D v = new Vector2D(p2.x - p1.x, p2.y - p1.y); // Direction vector
        Vector2D n = new Vector2D(-v.y, v.x); // Normal vector
        v.normalize();
        n.normalize();

        int h = (int) (10 * Math.sqrt(width));
        int w = (int) (5 * Math.sqrt(width));

        Vector2D middle = Vector2D.middle(p1, p2);

        Vector2D head = middle.plus(v.multiply(w / 2)); // head of the arrow
        Vector2D a = middle.minus(v.multiply(w / 2)).plus(n.multiply(h / 2));
        Vector2D b = middle.minus(v.multiply(w / 2)).minus(n.multiply(h / 2));

        paintLine(d, g, head, a);
        paintLine(d, g, head, b);
    }
}
