package Inforeg.ObjetGraph;

import Inforeg.Draw.Draw;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import Inforeg.UI.Vector2D;

/**
 * Clou sur un arc
 *
 * @author François MARIE
 * @author Rémi RAVELLI
 */

public class Nail extends Ellipse2D.Double implements Attachable, Clickable, GraphObject {

    public final static int DEFAULT_RADIUS = 3;
    public final static int HITBOX_RADIUS = 7;

    public Arc arc;

    public double cx;
    public double cy;
    public Vector2D prevPos;
    public double r;
    public Color color;
    public boolean selected = false;

    public Nail(double cx, double cy) {
        this.cx = cx;
        this.cy = cy;
        this.r = DEFAULT_RADIUS;
        this.color = null;
    }

    public Nail(double cx, double cy, double r) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = null;
    }

    public Nail(double cx, double cy, double r, Color color) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = color;
    }

    public Nail(double cx, double cy, Color color) {
        this.cx = cx;
        this.cy = cy;
        this.r = DEFAULT_RADIUS;
        this.color = color;
    }

    public Nail(double cx, double cy, Arc arc) {
        this.cx = cx;
        this.cy = cy;
        this.r = DEFAULT_RADIUS;
        this.color = arc.getColor();
        this.arc = arc;
    }

    public Nail(double cx, double cy, double r, Color color, Arc arc) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
        this.color = color;
        this.arc = arc;
    }

    @Override
    public void paint(Draw d, Graphics2D g) {
        Vector2D v = d.toDrawCoordinates(cx - r, cy - r);
        this.x = v.x;
        this.y = v.y;
        double h = d.toDrawScale(2 * r);
        this.height = h;
        this.width = h;

        g.setStroke(new BasicStroke((float) d.toDrawScale(3)));
        g.setColor(color);
        g.fill(this);
        //Outline
        if (selected) {
            g.setPaint(Color.GREEN);
            g.draw(this);
        }

    }

    public double getCx() {
        return cx;
    }

    public double getCy() {
        return cy;
    }

    public double getR() {
        return r;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public void delete() {
        if (arc != null && arc.getFrom() != arc.getTo()) {
            arc.getNails().remove(this);
        }
    }

    @Override
    public Vector2D getCenterPos() {
        return new Vector2D(cx, cy);
    }

    @Override
    public boolean contains(double x, double y) {
        return (((x - cx) * (x - cx) + (y - cy) * (y - cy)) <= HITBOX_RADIUS * HITBOX_RADIUS);
    }

    @Override
    public double getRadius() {
        return r;
    }

    @Override
    public String toString() {
        return "Clou | " + getArcIndex() + " " + arc.toString();
    }
    /**
     * @return l'index de ce clou dans la liste des clous de l'arc.
     */
    public int getArcIndex() {
        return arc.getNails().indexOf(this);
    }
    
}
